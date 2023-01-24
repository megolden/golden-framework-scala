package golden.framework.bind

import golden.framework.{Type, typeOf}
import ServiceLifetime.Scope
import collection.mutable

private class ContainerBuilderImpl extends ContainerBuilder:

  private val _registry = mutable.ArrayBuffer.empty[ServiceRegistrationBuilderImpl]

  override def register(tpe: Type, provider: ServiceProvider): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(tpe, provider)
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

  override def build(tags: Any*): Container =
    new ContainerImpl(buildServiceDescriptors(), tags*)
