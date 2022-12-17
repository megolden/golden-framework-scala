package golden.framework.bind

import golden.framework.{TypeInfo, typeOf, ReflectionUtils}
import ServiceLifetime.*

trait ContainerBuilder:

  def registerType(tpe: TypeInfo): ServiceRegistrationBuilder

  final inline def registerType[T]: ServiceRegistrationBuilder =
    registerType(typeOf[T]).usingConstructor(TypeServiceProvider.resolveConstructor[T]() *)

  private[bind] def registerInstance(instance: Any, tpe: TypeInfo): ServiceRegistrationBuilder

  final inline def registerInstance[T](instance: T): ServiceRegistrationBuilder =
    registerInstance(instance, typeOf[T])

  private[bind] def register(provider: Container => Any, tpe: TypeInfo): ServiceRegistrationBuilder

  final inline def register[T](provider: Container => T): ServiceRegistrationBuilder =
    register(provider.apply(_), typeOf[T])

  final inline def registerService[T](): ServiceRegistrationBuilder = {
    val tpe = typeOf[T]
    val serviceAnnotations = ReflectionUtils.getAnnotations[T, service]
    registerService(tpe, serviceAnnotations)
  }

  final def registerModule(module: Module): ContainerBuilder = {
    module.load(this)
    this
  }

  def build(tags: Any*): Container

  final private[bind] def registerServiceWithConstructor(
    tpe: TypeInfo,
    serviceAnnotations: Iterable[service],
    ctorParameterTypes: Seq[TypeInfo]): ServiceRegistrationBuilder = {

    registerService(tpe, serviceAnnotations).usingConstructor(ctorParameterTypes*)
  }

  private def registerService(tpe: TypeInfo, serviceAnnotations: Iterable[service]): ServiceRegistrationBuilder = {
    if (serviceAnnotations.isEmpty)
      throw ServiceRegistrationException(s"type not annotated as service: $tpe")

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

object ContainerBuilder:
  def create(): ContainerBuilder = new ContainerBuilderImpl()
