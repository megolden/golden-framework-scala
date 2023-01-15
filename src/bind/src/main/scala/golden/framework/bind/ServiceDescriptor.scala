package golden.framework.bind

import golden.framework.Type

private class ServiceDescriptor(
  val serviceTypes: Set[Type],
  val provider: ServiceProvider,
  val lifetime: ServiceLifetime,
  val externallyOwned: Boolean = false)
