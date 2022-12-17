package golden.framework.bind

import golden.framework.{TypeInfo, typeOf}
import scala.collection.mutable
import java.io.Closeable
import ServiceLifetime.*

private[bind] class ContainerImpl private(
  registry: Seq[ServiceDescriptor],
  singletonInstances: mutable.Map[ServiceDescriptor, Any],
  val isRoot: Boolean,
  val tags: Set[Any],
  _root: Option[Container])
  extends Container:

  def this(registry: Seq[ServiceDescriptor], tags: Any*) =
    this(registry, mutable.Map.empty, isRoot = true, Set("root") ++ tags, _root = None)

  private val _scopedInstances = mutable.HashMap.empty[ServiceDescriptor, Any]
  private val _closeableInstances = mutable.ArrayBuffer.empty[Closeable]

  override val root: Container = _root.getOrElse(this)

  override def get(serviceType: TypeInfo): Any = serviceType match {
    case tpe if isLazyResolution(tpe) => resolveLazy(tpe)
    case tpe if isCollectionResolution(tpe) => resolveCollection(tpe)
    case tpe if isOptionResolution(tpe) => resolveOption(tpe)
    case _ => resolveLast(serviceType)
  }

  private def findMatchServices(tpe: TypeInfo): Seq[ServiceDescriptor] =
    registry.filter(_.serviceTypes.toSeq.contains(tpe))

  private def isLazyResolution(tpe: TypeInfo): Boolean =
    tpe.symbolName == typeOf[Lazy[?]].symbolName

  private def resolveLazy(lazyType: TypeInfo): Any = {
    val serviceType = lazyType.args.head
    val container = this
    new Lazy[Any] {
      override lazy val get: Any = container.get(serviceType)
    }
  }

  private def isOptionResolution(tpe: TypeInfo): Boolean =
    tpe.symbolName == typeOf[Option[?]].symbolName

  private def resolveOption(serviceType: TypeInfo): Option[?] =
    findMatchServices(serviceType.args.head).lastOption.map(get)

  private def resolveLast(serviceType: TypeInfo): Any =
    findMatchServices(serviceType).lastOption.map(get).getOrElse {
      throw ServiceResolutionException(serviceType)
    }

  private def isCollectionResolution(tpe: TypeInfo): Boolean =
    tpe.symbolName == typeOf[Iterable[?]].symbolName

  private def resolveCollection(serviceType: TypeInfo): Seq[?] =
    findMatchServices(serviceType.args.head).map(get)

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
