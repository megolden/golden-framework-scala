package golden.framework.bind

import golden.framework.Type
import scala.collection.mutable
import ServiceLifetime.*

private class ServiceRegistrationBuilderImpl(
  implementationType: Type,
  instance: Option[Any] = None,
  factory: Option[Container => Any] = None)
  extends ServiceRegistrationBuilder:

  private val _serviceTypes = mutable.ArrayBuffer.empty[Type]
  private var _lifetime = Transient
  private var _externallyOwned = false
  private var _ctorParams: Option[Seq[Type]] = None

  override def as(serviceType: Type): ServiceRegistrationBuilder = {
    _serviceTypes += serviceType
    this
  }

  override def asSelf(): ServiceRegistrationBuilder =
    as(implementationType)

  override def usingConstructor(params: Type*): ServiceRegistrationBuilder = {
    _ctorParams = Some(params)
    this
  }

  override def asSingleton(): ServiceRegistrationBuilder = {
    _lifetime = Singleton
    this
  }

  override def asContainerScoped(): ServiceRegistrationBuilder = {
    _lifetime = Scope
    this
  }

  override def withLifetime(lifetime: ServiceLifetime): ServiceRegistrationBuilder = {
    _lifetime = lifetime
    this
  }

  override def externallyOwned(): ServiceRegistrationBuilder = {
    _externallyOwned = true
    this
  }

  def build(): ServiceDescriptor = {
    val serviceTypes = if _serviceTypes.isEmpty then Set(implementationType) else _serviceTypes.toSet
    val provider: ServiceProvider =
      instance.map(InstanceServiceProvider(implementationType, _))
      .orElse(factory.map(FactoryServiceProvider(implementationType, _)))
      .getOrElse {
        if _ctorParams.isEmpty then TypeServiceProvider(implementationType)
        else TypeServiceProvider(implementationType, _ctorParams.get)
      }
    ServiceDescriptor(serviceTypes, provider, _lifetime, _externallyOwned)
  }
