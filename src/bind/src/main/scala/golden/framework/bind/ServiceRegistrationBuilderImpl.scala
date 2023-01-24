package golden.framework.bind

import golden.framework.Type
import scala.collection.mutable
import ServiceLifetime.*

private class ServiceRegistrationBuilderImpl(implementationType: Type, provider: ServiceProvider)
  extends ServiceRegistrationBuilder:

  private val _serviceTypes = mutable.HashSet.empty[Type]
  private var _lifetime = Transient
  private var _externallyOwned = false

  override def as(serviceType: Type): ServiceRegistrationBuilder = {
    _serviceTypes += serviceType
    this
  }

  override def asSelf(): ServiceRegistrationBuilder =
    as(implementationType)

  override def asSingleton(): ServiceRegistrationBuilder = {
    _lifetime = Singleton
    this
  }

  override def asContainerScoped(): ServiceRegistrationBuilder = {
    _lifetime = Scope
    this
  }

  override def externallyOwned(): ServiceRegistrationBuilder = {
    _externallyOwned = true
    this
  }

  def build(): ServiceDescriptor = {
    val serviceTypes = if _serviceTypes.isEmpty then Set(implementationType) else _serviceTypes.toSet
    ServiceDescriptor(serviceTypes, provider, _lifetime, _externallyOwned)
  }
