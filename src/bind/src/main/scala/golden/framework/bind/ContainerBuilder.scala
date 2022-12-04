package golden.framework.bind

import golden.framework.{TypeInfo, typeOf, ReflectionUtils}
import scala.collection.mutable
import ServiceLifetime.*

class ContainerBuilder:

  private val _registry = mutable.ArrayBuffer.empty[ServiceRegistrationBuilder]

  def registerType(tpe: TypeInfo): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(tpe)
    _registry += builder
    builder
  }

  inline def registerType[T]: ServiceRegistrationBuilder =
    registerType(typeOf[T]).usingConstructor(TypeServiceProvider.resolveConstructor[T]() *)

  inline def registerInstance[T](instance: T): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(typeOf[T], instance = Some(instance))
    _registry += builder
    builder
  }

  inline def register[T](provider: Container => T): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(typeOf[T], factory = Some(provider))
    _registry += builder
    builder
  }

  def registerModule(module: Module): ContainerBuilder = {
    module.load(this)
    this
  }

  private def registerService(tpe: TypeInfo, serviceAnnotations: Iterable[service]): ServiceRegistrationBuilder = {
    if (serviceAnnotations.isEmpty)
      throw ServiceRegistrationException(s"type '$tpe' not annotated as service")

    var builder = registerType(tpe)
    serviceAnnotations.foreach { service =>
      if (service.as.isEmpty)
        builder = builder.asSelf()
      else
        builder = builder.as(service.as.get)

      builder = service.lifetime match
        case Singleton => builder.asSingleton()
        case Scope => builder.asContainerScoped()
        case _ => builder
    }
    builder
  }

  inline def registerService[T](): ServiceRegistrationBuilder =
    registerService(typeOf[T], ReflectionUtils.getAnnotations[T, service])

  private[bind] def registerServiceWithConstructor(
    tpe: TypeInfo,
    serviceAnnotations: Iterable[service],
    ctorParameterTypes: Seq[TypeInfo]): ServiceRegistrationBuilder = {

    registerService(tpe, serviceAnnotations).usingConstructor(ctorParameterTypes*)
  }

  private[bind] def buildServiceDescriptors(): Seq[ServiceDescriptor] = {
    val containerService = ServiceDescriptor(
      Some(typeOf[Container]),
      FactoryServiceProvider(typeOf[Container], container => container),
      Scope)
    _registry.toSeq.map(_.build()) :+ containerService
  }

  def build(tags: Any*): Container =
    new ContainerImpl(buildServiceDescriptors(), tags*)
