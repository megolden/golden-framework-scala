package golden.framework.bind

import golden.framework.{TypeInfo, typeOf}
import ServiceLifetime.Scope
import collection.mutable.ArrayBuffer as MutableArray

private[bind] class ContainerBuilderImpl extends ContainerBuilder:

  private val _registry = MutableArray.empty[ServiceRegistrationBuilderImpl]

  def registerType(tpe: TypeInfo): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(tpe)
    _registry += builder
    builder
  }

  def registerInstance(instance: Any, tpe: TypeInfo): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(tpe, Some(instance))
    _registry += builder
    builder
  }

  def register(provider: Container => Any, tpe: TypeInfo): ServiceRegistrationBuilder = {
    val builder = ServiceRegistrationBuilderImpl(tpe, factory = Some(provider))
    _registry += builder
    builder
  }

  def buildServiceDescriptors(): Seq[ServiceDescriptor] = {
    val containerService = ServiceDescriptor(
      Some(typeOf[Container]),
      FactoryServiceProvider(typeOf[Container], container => container),
      Scope)
    _registry.toSeq.map(_.build()) :+ containerService
  }

  def build(tags: Any*): Container =
    new ContainerImpl(buildServiceDescriptors(), tags *)
