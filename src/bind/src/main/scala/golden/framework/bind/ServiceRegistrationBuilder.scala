package golden.framework.bind

import golden.framework.{TypeInfo, typeOf}

trait ServiceRegistrationBuilder:

  def as(serviceType: TypeInfo): ServiceRegistrationBuilder

  final def as(serviceType: Class[?]): ServiceRegistrationBuilder =
    as(TypeInfo.fromClass(serviceType))

  inline final def as[T](): ServiceRegistrationBuilder =
    as(typeOf[T])

  def asSelf(): ServiceRegistrationBuilder

  def usingConstructor(argTypes: TypeInfo*): ServiceRegistrationBuilder

  def asSingleton(): ServiceRegistrationBuilder

  def asContainerScoped(): ServiceRegistrationBuilder

  def withLifetime(lifetime: ServiceLifetime): ServiceRegistrationBuilder

  def externallyOwned(): ServiceRegistrationBuilder
