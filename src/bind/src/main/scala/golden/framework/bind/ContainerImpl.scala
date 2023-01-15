package golden.framework.bind

import golden.framework.{Type, typeOf, ParameterizedType}
import scala.collection.mutable
import java.io.Closeable
import ServiceLifetime.*

private class ContainerImpl private(
  registry: Seq[ServiceDescriptor],
  singletonInstances: mutable.Map[ServiceDescriptor, Any],
  val isRoot: Boolean,
  val tags: Set[Any],
  rootContainer: Option[Container])
  extends Container:

  def this(registry: Seq[ServiceDescriptor], tags: Any*) =
    this(registry, mutable.Map.empty, isRoot = true, Set("root") ++ tags, rootContainer = None)

  private val _scopedInstances = mutable.HashMap.empty[ServiceDescriptor, Any]
  private val _closeableInstances = mutable.ArrayBuffer.empty[Closeable]

  override val root: Container = rootContainer.getOrElse(this)

  override def get(serviceType: Type): Any = serviceType match {
    case tpe if isLazyResolution(tpe) =>
      resolveLazy(tpe.asInstanceOf[ParameterizedType].args.head)
    case tpe if isCollectionResolution(tpe) =>
      resolveCollection(tpe.asInstanceOf[ParameterizedType].args.head)
    case tpe if isOptionResolution(tpe) =>
      resolveOption(tpe.asInstanceOf[ParameterizedType].args.head)
    case _ =>
      resolveLast(serviceType)
  }

  private def findMatchServices(tpe: Type): Seq[ServiceDescriptor] =
    registry.filter(_.serviceTypes.contains(tpe))

  private def isLazyResolution(tpe: Type): Boolean =
    tpe.symbolName == typeOf[Lazy[?]].symbolName

  private def resolveLazy(serviceType: Type): Any = {
    val container = this
    new Lazy[Any] {
      override lazy val get: Any = container.get(serviceType)
    }
  }

  private def isOptionResolution(tpe: Type): Boolean =
    tpe.symbolName == typeOf[Option[?]].symbolName

  private def resolveOption(serviceType: Type): Option[?] =
    findMatchServices(serviceType).lastOption.map(get)

  private def resolveLast(serviceType: Type): Any =
    findMatchServices(serviceType).lastOption.map(get).getOrElse {
      throw ServiceResolutionException(serviceType)
    }

  private def isCollectionResolution(tpe: Type): Boolean =
    tpe.symbolName == typeOf[Iterable[?]].symbolName

  private def resolveCollection(serviceType: Type): Seq[Any] =
    findMatchServices(serviceType).map(get)

  private def get(descriptor: ServiceDescriptor): Any = {
    val instance = descriptor.lifetime match {
      case Singleton => singletonInstances.getOrElseUpdate(descriptor, descriptor.provider.get(this))
      case Scope => _scopedInstances.getOrElseUpdate(descriptor, descriptor.provider.get(this))
      case _ => descriptor.provider.get(this)
    }
    addToCloseables(instance, descriptor)
    instance
  }

  private def addToCloseables(instance: Any, descriptor: ServiceDescriptor): Unit = {
    if (instance != this && !descriptor.externallyOwned && instance.isInstanceOf[Closeable]) {
      if (descriptor.lifetime != Singleton || isRoot)
        _closeableInstances += instance.asInstanceOf[Closeable]
    }
  }

  override def createScope(additionalSetup: ContainerBuilder => ?, tags: Any*): Container =
    val builder = new ContainerBuilderImpl
    additionalSetup(builder)
    val additionalDescriptors = builder.buildServiceDescriptors()
    new ContainerImpl(registry ++ additionalDescriptors, singletonInstances, isRoot = false, tags.toSet, Some(root))

  override def close(): Unit =
    _closeableInstances.toSeq.foreach(_.close())
