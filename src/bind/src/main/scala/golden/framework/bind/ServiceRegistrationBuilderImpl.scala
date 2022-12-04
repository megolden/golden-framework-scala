package golden.framework.bind

import golden.framework.TypeInfo
import scala.collection.mutable
import ServiceLifetime.*

private[bind] class ServiceRegistrationBuilderImpl(
  implementationType: TypeInfo,
  instance: Option[Any] = None,
  factory: Option[Container => Any] = None)
  extends ServiceRegistrationBuilder:

  private val _serviceTypes = mutable.ArrayBuffer.empty[TypeInfo]
  private var _lifetime = Transient
  private var _externallyOwned = false
  private var _constructorParameterTypes = Seq.empty[TypeInfo]

  override def as(serviceType: TypeInfo): ServiceRegistrationBuilder = {
    _serviceTypes += serviceType
    this
  }

  override def asSelf(): ServiceRegistrationBuilder =
    as(implementationType)

  override def usingConstructor(argTypes: TypeInfo*): ServiceRegistrationBuilder = {
    _constructorParameterTypes = argTypes
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

  override def build(): ServiceDescriptor = {
    val serviceTypes = if _serviceTypes.isEmpty then Seq(implementationType) else _serviceTypes
    val provider: ServiceProvider =
      instance.map(InstanceServiceProvider(implementationType, _))
      .orElse(factory.map(FactoryServiceProvider(implementationType, _)))
      .getOrElse(TypeServiceProvider(implementationType, _constructorParameterTypes))
    ServiceDescriptor(serviceTypes, provider, _lifetime, _externallyOwned)
  }
