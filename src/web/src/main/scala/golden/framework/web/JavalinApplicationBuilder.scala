package golden.framework.web

import golden.framework.bind.{Container, ContainerBuilder, Module}
import golden.framework.json.JsonMapper
import golden.framework.{Environment, StartupTask, Type}
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.javalin.http.{Handler, HandlerType}
import io.javalin.util.JavalinLogger
import golden.framework.OptionExtensions.tap
import io.javalin.json.JsonMapper as JavalinJsonMapper
import java.io.{ByteArrayInputStream, InputStream}
import java.lang.reflect.Type as JType
import java.nio.charset.StandardCharsets.UTF_8
import scala.collection.mutable.{ArrayBuffer as MutableArray, HashMap as MutableMap}

private class JavalinApplicationBuilder extends ApplicationBuilder:

  private var _scopedMethods = Seq("POST", "PUT", "PATCH", "DELETE", "GET")
  private val _modules = MutableArray.empty[Module]
  private val _handlerTypes = MutableMap.empty[Type, Iterable[HttpMethodAnnotation]]
  private val _handlers = MutableArray.empty[(String, RequestHandler, Iterable[HandlerType])]
  private var _commandLineArgs = Seq.empty[String]
  private var _logRequests: Option[Boolean] = None
  private var _enableLogging = true

  def registerModule(module: Module): ApplicationBuilder = {
    _modules += module
    this
  }

  def requestLogging(enable: Boolean): ApplicationBuilder = {
    _logRequests = Some(enable)
    this
  }

  def logging(enable: Boolean): ApplicationBuilder = {
    _enableLogging = enable
    this
  }

  def addHandlerType(handlerType: Type, methods: Iterable[HttpMethodAnnotation]): ApplicationBuilder = {
    _handlerTypes.update(handlerType, methods)
    this
  }

  def addHandler(method: String, path: String, handler: RequestHandler): ApplicationBuilder = {
    _handlers += ((path, handler, method.toUpperCase.split(',').map(HandlerType.valueOf)))
    this
  }

  def setCommandLineArgs(args: Seq[String]): ApplicationBuilder = {
    _commandLineArgs = args
    this
  }

  def scopedRequestTypes(methods: String*): ApplicationBuilder = {
    _scopedMethods = methods.toSeq
    this
  }

  def build(): Application =
    buildJavalinApplication()

  private def buildJavalinApplication(): Application = {
    JavalinLogger.startupInfo = false
    JavalinLogger.enabled = _enableLogging

    val container = createContainer()

    val startupTasks = container.get[Iterable[StartupTask]].toSeq.sortBy(_.order)
    startupTasks.filter(_.beforeStart).foreach(_.execute())

    val environment = container.get[WebEnvironment]
    val jsonMapper = container.get[JsonMapper]
    val javalinApp = Javalin.create { config =>

      config.showJavalinBanner = false

      config.jsonMapper(createJsonMapper(jsonMapper))

      if _logRequests.getOrElse(environment.isDevelopment) then
        config.plugins.enableDevLogging()

      if environment.isDevelopment then
        config.plugins.enableCors(cors => cors.add(_.anyHost()))

      if environment.webRootDir.nonEmpty then
        config.staticFiles.add(environment.webRootDir.get, Location.EXTERNAL)
        if environment.isSPAFront.getOrElse(false) then
          config.spaRoot.addFile("/", "index.html", Location.EXTERNAL)
    }

    val app = createJavalinApplication(javalinApp, container)
    container.get[ApplicationWrapper].set(app)

    startupTasks.filterNot(_.beforeStart).foreach(_.execute())

    configureRequestScopeStart(javalinApp, container)
    registerHandlers(javalinApp)
    configureRequestScopeEnd(javalinApp)

    app
  }

  private def createContainer(): Container = {
    val builder = ContainerBuilder.create()
    _modules.foreach(_.load(builder))
    registerCommonServices(builder, _commandLineArgs)
    builder.build()
  }

  private def registerCommonServices(builder: ContainerBuilder, commandLineArgs: Seq[String]): Unit = {
    val _commandLineArgs = commandLineArgs
    val defaultEnvironment = new WebEnvironment {
      val isDevelopment: Boolean = true
      val isProduction: Boolean = false
      val commandLineArgs: Seq[String] = _commandLineArgs
      val currentDir: String = sys.props.get("user.dir").get
      val webRootDir: Option[String] = None
      val isSPAFront: Option[Boolean] = None
    }

    builder.registerInstance(defaultEnvironment).as[WebEnvironment]().as[Environment]().asSingleton()
    builder.registerInstance(new JsonMapper()).as[JsonMapper]().asSingleton()
    builder.registerInstance(new ApplicationWrapper()).as[ApplicationWrapper]().asSingleton()
    builder.register(container => container.tags.collectFirst { case context: HttpContext => context }.get)
      .as[HttpContext]()
      .asContainerScoped()
  }

  private def createJsonMapper(jsonMapper: JsonMapper): JavalinJsonMapper = {
    new JavalinJsonMapper {

      override def toJsonString(obj: Any, tpe: JType): String =
        jsonMapper.serialize(obj)

      override def toJsonStream(obj: Any, tpe: JType): InputStream =
        ByteArrayInputStream(toJsonString(obj, tpe).getBytes(UTF_8))

      override def fromJsonString[T](json: String, tpe: JType): T =
        jsonMapper.deserialize[T](json, tpe)

      override def fromJsonStream[T](json: InputStream, tpe: JType): T =
        jsonMapper.deserialize[T](json, tpe)
    }
  }

  private def createJavalinApplication(app: Javalin, container: Container): Application = {
    new Application {

      def start(host: Option[String], port: Option[Int]): Application = {
        if host.nonEmpty then app.start(host.get, port.get)
        else if port.nonEmpty then app.start(port.get)
        else app.start()
        this
      }

      def stop(): Application = {
        container.close()
        app.stop()
        sys.exit()
        this
      }
    }
  }

  private def configureRequestScopeStart(app: Javalin, container: Container): Unit = {
    app.before { context =>
      if _scopedMethods.exists(_.equalsIgnoreCase(context.method.name)) then {
        val httpContext = JavalinHttpContext(context)
        val scope = container.createScope(httpContext)
        httpContext.attribute("scope", scope)
        httpContext.attribute("httpContext", httpContext)
      }
    }
  }

  private def configureRequestScopeEnd(app: Javalin): Unit = {
    app.after { context =>
      if _scopedMethods.exists(_.equalsIgnoreCase(context.method.name)) then {
        val scope = Option(context.attribute[Container]("scope"))
        scope.tap(_.close())
      }
    }
  }

  private def registerHandlers(app: Javalin): Unit = {

    _handlers.toSeq
      .flatMap { case (path, handler, handlerTypes) => handlerTypes.map((path, handler) -> _) }
      .foreach { case ((path, handler), handlerType) =>
        val javalinHandler: Handler = { context =>
          val httpContext = context.attribute[HttpContext]("httpContext")
          handler.handle(httpContext)
        }
        app.addHandler(handlerType, path, javalinHandler)
      }

    _handlerTypes.toSeq
      .flatMap { case (handlerType, methods) => methods.map(handlerType -> _) }
      .sortBy { case (_, method) => method.order }
      .foreach { case (reqHandlerType, httpMethod) =>
        val handlerType = toJavalinHandlerType(httpMethod)
        val handler = createJavalinHandler(reqHandlerType)
        app.addHandler(handlerType, httpMethod.path, handler)
      }

    def createJavalinHandler(handlerType: Type): Handler = { context =>
      val httpContext = context.attribute[HttpContext]("httpContext")
      val scope = context.attribute[Container]("scope")
      val handler = scope.get(handlerType).asInstanceOf[RequestHandler]
      handler.handle(httpContext)
    }
  }

  private def toJavalinHandlerType(httpMethod: HttpMethodAnnotation): HandlerType = httpMethod match
    case _: httpGet => HandlerType.GET
    case _: httpPost => HandlerType.POST
    case _: httpPut => HandlerType.PUT
    case _: httpPatch => HandlerType.PATCH
    case _: httpDelete => HandlerType.DELETE
    case _ => throw IllegalArgumentException(s"invalid http method: ${httpMethod.getClass}")

  private class ApplicationWrapper:
    private var _app: Option[Application] = None

    def get: Application = _app.getOrElse {
      throw IllegalStateException("application not started yet")
    }

    def set(app: Application): Unit = _app = Some(app)
