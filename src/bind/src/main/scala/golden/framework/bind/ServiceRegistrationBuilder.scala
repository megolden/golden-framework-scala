package golden.framework.bind

import golden.framework.{Type, typeOf}

trait ServiceRegistrationBuilder:

  def as(serviceType: Type): ServiceRegistrationBuilder

  inline final def as[T](): ServiceRegistrationBuilder =
    as(typeOf[T])

  def asSelf(): ServiceRegistrationBuilder

  def asSingleton(): ServiceRegistrationBuilder

  def asContainerScoped(): ServiceRegistrationBuilder

  def externallyOwned(): ServiceRegistrationBuilder
