package golden.framework.bind

import golden.framework.{Type, ParameterizedType}
import scala.collection.mutable
import java.io.Closeable
import ServiceLifetime.*

private class ContainerImpl private(
  registry: Seq[ServiceDescriptor],
  val tags: Set[Any],
  rootContainer: Option[ContainerImpl])
  extends Container:

  private val _instances = mutable.LinkedHashMap.empty[ServiceDescriptor, Any]

  def this(registry: Seq[ServiceDescriptor], tags: Any*) =
    this(registry, Set("root") ++ tags, rootContainer = None)

  override val isRoot: Boolean = rootContainer.isEmpty

  override val root: ContainerImpl = rootContainer.getOrElse(this)

  override def get(serviceType: Type): Any = serviceType match {
    case tpe if isLazyResolution(tpe) =>
      resolveLazy(tpe.asInstanceOf[ParameterizedType].args.head)
    case tpe if isCollectionResolution(tpe) =>
      resolveAll(tpe.asInstanceOf[ParameterizedType].args.head)
    case tpe if isOptionResolution(tpe) =>
      resolveOption(tpe.asInstanceOf[ParameterizedType].args.head)
    case _ =>
      resolveLast(serviceType)
  }

  private def findMatchServices(tpe: Type): Seq[ServiceDescriptor] =
    registry.filter(_.serviceTypes.contains(tpe))

  private def isLazyResolution(tpe: Type): Boolean =
    tpe.rawType == classOf[Lazy[?]]

  private def resolveLazy(serviceType: Type): Any = {
    val container = this
    new Lazy[Any] {
      override lazy val get: Any = container.get(serviceType)
    }
  }

  private def isOptionResolution(tpe: Type): Boolean =
    tpe.rawType == classOf[Option[?]]

  private def resolveOption(serviceType: Type): Option[?] =
    findMatchServices(serviceType).lastOption.map(getInstance)

  private def resolveLast(serviceType: Type): Any =
    findMatchServices(serviceType).lastOption.map(getInstance).getOrElse {
      throw ServiceResolutionException(serviceType)
    }

  private def isCollectionResolution(tpe: Type): Boolean =
    tpe.rawType == classOf[List[?]]

  private def resolveAll(serviceType: Type): List[Any] =
    findMatchServices(serviceType).map(getInstance).toList

  private def getInstance(descriptor: ServiceDescriptor): Any = descriptor.lifetime match {
    case Singleton => root._instances.getOrElseUpdate(descriptor, descriptor.provider.get(this))
    case Scope => _instances.getOrElseUpdate(descriptor, descriptor.provider.get(this))
    case Transient => _instances.getOrElseUpdate(descriptor.clone(), descriptor.provider.get(this))
  }

  override def createScope(additionalSetup: ContainerBuilder => ?, tags: Any*): Container = {
    val builder = new ContainerBuilderImpl
    additionalSetup(builder)
    val additionalDescriptors = builder.buildServiceDescriptors()
    new ContainerImpl(registry ++ additionalDescriptors, tags.toSet, Some(root))
  }

  override def close(): Unit = {
    _instances.collect { case (descriptor, instance: Closeable)
      if !descriptor.externallyOwned && !instance.isInstanceOf[Container] => instance
    } .toSeq foreach {
      _.close()
    }
  }
