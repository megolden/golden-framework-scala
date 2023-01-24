package golden.framework.bind

object ContainerBuilderExtensions:

  extension (builder: ContainerBuilder)

    inline def registerPackageServices[TPackageRoot](setup: ServiceRegistrationBuilder => ?): ContainerBuilder = {
      val serviceTypes = Macros.findAnnotatedServicesOf[TPackageRoot]
      serviceTypes.foreach { (tpe, constructor, services) =>
        val provider = new TypeServiceProvider(tpe, constructor)
        val serviceBuilder = builder.registerService(tpe, provider, services)
        setup.apply(serviceBuilder)
      }
      builder
    }

    inline def registerPackageServices[TPackageRoot](): ContainerBuilder =
      registerPackageServices[TPackageRoot](_ => ())
