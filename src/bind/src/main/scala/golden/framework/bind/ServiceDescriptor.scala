package golden.framework.bind

import golden.framework.TypeInfo

private[bind] class ServiceDescriptor(
  val serviceTypes: Iterable[TypeInfo],
  val provider: ServiceProvider,
  val lifetime: ServiceLifetime,
  val externallyOwned: Boolean = false)
