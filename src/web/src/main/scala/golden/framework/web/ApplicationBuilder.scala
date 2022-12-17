package golden.framework.web

import golden.framework.bind.Module
import golden.framework.{Macros, TypeInfo, typeOf}

trait ApplicationBuilder:

  def registerModule(module: Module): ApplicationBuilder

  def requestLogging(enable: Boolean): ApplicationBuilder

  def logging(enable: Boolean): ApplicationBuilder

  private[web] def addHandlerType(handlerType: TypeInfo, methods: Iterable[HttpMethodAnnotation]): ApplicationBuilder

  final inline def addHandler[THandler <: RequestHandler](): ApplicationBuilder = {
    val handlerType = typeOf[THandler]
    val methods = Macros.getAnnotations[THandler, HttpMethodAnnotation].toSeq
    addHandlerType(handlerType, methods)
  }

  def addHandler(method: String, path: String, handler: HttpContext => Unit): ApplicationBuilder

  final def addGetHandler(path: String, handler: HttpContext => Unit): ApplicationBuilder =
    addHandler("GET", path, handler)

  final def addPostHandler(path: String, handler: HttpContext => Unit): ApplicationBuilder =
    addHandler("POST", path, handler)

  final def addDeleteHandler(path: String, handler: HttpContext => Unit): ApplicationBuilder =
    addHandler("DELETE", path, handler)

  final def addPatchHandler(path: String, handler: HttpContext => Unit): ApplicationBuilder =
    addHandler("PATCH", path, handler)

  final def addPutHandler(path: String, handler: HttpContext => Unit): ApplicationBuilder =
    addHandler("PUT", path, handler)

  def setCommandLineArgs(args: Seq[String]): ApplicationBuilder

  def scopedRequestTypes(methods: String*): ApplicationBuilder

  def build(): Application

object ApplicationBuilder:
  def create(): ApplicationBuilder = new JavalinApplicationBuilder
