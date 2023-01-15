package golden.framework.bind

import golden.framework.{Type, typeOf}
import ServiceLifetime.Scope
import collection.mutable

private class ContainerBuilderImpl extends ContainerBuilder:

  private val _registry = mutable.ArrayBuffer.empty[ServiceRegistrationBuilderImpl]

  def registerType(tpe: Type): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(tpe)
    _registry += builder
    builder
  }

  def registerInstance(instance: Any, tpe: Type): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(tpe, Some(instance))
    _registry += builder
    builder
  }

  def register(provider: Container => Any, tpe: Type): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(tpe, factory = Some(provider))
    _registry += builder
    builder
  }

  def buildServiceDescriptors(): Seq[ServiceDescriptor] = {
    val containerService = ServiceDescriptor(
      Set(typeOf[Container]),
      FactoryServiceProvider(typeOf[Container], container => container),
      Scope)
    _registry.toSeq.map(_.build()) :+ containerService
  }

  def build(tags: Any*): Container =
    new ContainerImpl(buildServiceDescriptors(), tags*)
