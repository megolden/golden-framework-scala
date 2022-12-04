package golden.framework.bind

import ServiceLifetime.Transient

object ContainerBuilderExtensions:
  extension (builder: ContainerBuilder)

    inline def registerPackageServices[TPackageRoot](setup: ServiceRegistrationBuilder => ?): ContainerBuilder = {
      val services = Macros.getAnnotatedPackageTypes[TPackageRoot, service]
      services.foreach { case (tpe, serviceAnnotations, ctorParams) =>
        val serviceBuilder = builder.registerServiceWithConstructor(tpe, serviceAnnotations, ctorParams)
        setup(serviceBuilder)
      }
      builder
    }

    inline def registerPackageServices[TPackageRoot](lifetime: ServiceLifetime = Transient): ContainerBuilder =
      registerPackageServices[TPackageRoot](_.withLifetime(lifetime))
