package golden.framework.web

import golden.framework.bind.Module
import golden.framework.{ReflectionUtils, Type, typeOf}

trait ApplicationBuilder:

  def registerModule(module: Module): ApplicationBuilder

  def requestLogging(enable: Boolean): ApplicationBuilder

  def logging(enable: Boolean): ApplicationBuilder

  private[web] def addHandlerType(handlerType: Type, methods: Iterable[HttpMethodAnnotation]): ApplicationBuilder

  final inline def addHandler[THandler <: RequestHandler](): ApplicationBuilder = {
    val handlerType = typeOf[THandler]
    val methods = ReflectionUtils.annotationsOf[THandler, HttpMethodAnnotation]
    addHandlerType(handlerType, methods)
  }

  def addHandler(method: String, path: String, handler: RequestHandler): ApplicationBuilder

  final def addGetHandler(path: String, handler: RequestHandler): ApplicationBuilder =
    addHandler("GET", path, handler)

  final def addPostHandler(path: String, handler: RequestHandler): ApplicationBuilder =
    addHandler("POST", path, handler)

  final def addDeleteHandler(path: String, handler: RequestHandler): ApplicationBuilder =
    addHandler("DELETE", path, handler)

  final def addPatchHandler(path: String, handler: RequestHandler): ApplicationBuilder =
    addHandler("PATCH", path, handler)

  final def addPutHandler(path: String, handler: RequestHandler): ApplicationBuilder =
    addHandler("PUT", path, handler)

  def setCommandLineArgs(args: Seq[String]): ApplicationBuilder

  def scopedRequestTypes(methods: String*): ApplicationBuilder

  def build(): Application

object ApplicationBuilder:
  def create(): ApplicationBuilder = new JavalinApplicationBuilder
