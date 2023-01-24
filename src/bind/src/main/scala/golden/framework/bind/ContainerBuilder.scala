package golden.framework.bind

import golden.framework.{Type, typeOf, ReflectionUtils}

trait ContainerBuilder:

  private[bind] def register(tpe: Type, provider: ServiceProvider): ServiceRegistrationBuilder

  final def registerType(tpe: Type): ServiceRegistrationBuilder = {
    val provider = TypeServiceProvider(tpe)
    register(tpe, provider)
  }


  final inline def registerType[T]: ServiceRegistrationBuilder = {
    val tpe = typeOf[T]
    val constructor = Macros.findConstructorOf[T]
    val provider = TypeServiceProvider(tpe, constructor)
    register(tpe, provider)
  }

  final inline def registerInstance[T](instance: T): ServiceRegistrationBuilder = {
    val tpe = typeOf[T]
    val provider = InstanceServiceProvider(tpe, instance)
    register(tpe, provider)
  }

  final inline def register[T](factory: Container => T): ServiceRegistrationBuilder = {
    val tpe = typeOf[T]
    val provider = FactoryServiceProvider(tpe, factory.apply(_))
    register(tpe, provider)
  }

  final inline def registerService[T](): ServiceRegistrationBuilder = {
    val tpe = typeOf[T]
    val constructor = Macros.findConstructorOf[T]
    val provider = TypeServiceProvider(tpe, constructor)
    val services = ReflectionUtils.annotationsOf[T, service]
    registerService(tpe, provider, services)
  }

  final def registerModule(module: Module): ContainerBuilder = {
    module.load(this)
    this
  }

  def build(tags: Any*): Container

  private[bind] def registerService(
    tpe: Type,
    provider: ServiceProvider,
    services: Iterable[service]
  ): ServiceRegistrationBuilder = {

    if (services.isEmpty)
      throw ServiceRegistrationException(s"type not annotated as service: $tpe")

    var builder = register(tpe, provider)
    services.foreach { service =>
      if service.as.nonEmpty then builder = builder.as(service.as.get)
      else builder = builder.asSelf()
      if service.asSingleton then builder = builder.asSingleton()
      else if service.asContainerScoped then builder = builder.asContainerScoped()
    }
    builder
  }

object ContainerBuilder:
  def create(): ContainerBuilder = new ContainerBuilderImpl()
