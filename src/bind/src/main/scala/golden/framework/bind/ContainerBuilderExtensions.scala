package golden.framework.bind

import ServiceLifetime.Transient

object ContainerBuilderExtensions:
  extension (builder: ContainerBuilder)

    inline def registerPackageServices[TPackageRoot](setup: ServiceRegistrationBuilder => ?): ContainerBuilder = {
      val services = Macros.getAnnotatedPackageTypesWithCtor[TPackageRoot, service]
      services.foreach { (tpe, serviceAnnotations, ctorParams) =>
        val serviceBuilder = builder.registerService(tpe, serviceAnnotations).usingConstructor(ctorParams*)
        setup(serviceBuilder)
      }
      builder
    }

    inline def registerPackageServices[TPackageRoot](lifetime: ServiceLifetime = Transient): ContainerBuilder =
      registerPackageServices[TPackageRoot](_.withLifetime(lifetime))
