package golden.framework.web

import golden.framework.typeOf
import java.lang.reflect.Type
import java.net.HttpURLConnection.{HTTP_BAD_REQUEST, HTTP_INTERNAL_ERROR, HTTP_NO_CONTENT}

trait HttpContext:

  def url: String

  def status(statusCode: Int): HttpContext

  final def noContent(): HttpContext =
    status(HTTP_NO_CONTENT)

  final def badRequest(): HttpContext =
    status(HTTP_BAD_REQUEST)

  final def internalError(): HttpContext =
    status(HTTP_INTERNAL_ERROR)

  def json(obj: Any): HttpContext

  def result(resultString: String): HttpContext

  def result(resultBytes: Array[Byte]): HttpContext

  def contentType(contentType: String): HttpContext

  inline final def bodyAs[T]: T =
    bodyAs[T](typeOf[T].asClass.asInstanceOf[Class[T]])

  protected def bodyAs[T](clazz: Class[T]): T

  protected def bodyAs[T](tpe: Type): T

  def pathParam(name: String): Option[String]

  def formParam(name: String): Option[String]

  def queryParam(name: String): Option[String]

  def uploadedFile(name: String): Option[UploadedFile]

  def uploadedFile(index: Int): Option[UploadedFile]

  def header(name: String, value: String): HttpContext

  def header(name: String): Option[String]

  def attribute(name: String, value: Any): HttpContext

  def attribute[T](name: String): Option[T]
