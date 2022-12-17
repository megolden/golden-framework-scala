package golden.framework.web

import io.javalin.http.{Context, UploadedFile as JavalinUploadedFile}
import java.io.InputStream
import java.lang.reflect.Type

private[web] class JavalinHttpContext(private var context: Context) extends HttpContext:

  def url: String = context.fullUrl

  def status(statusCode: Int): HttpContext = {
    context = context.status(statusCode)
    this
  }

  def json(obj: Any): HttpContext = {
    context = context.json(obj)
    this
  }

  def result(resultString: String): HttpContext = {
    context = context.result(resultString)
    this
  }

  def result(resultBytes: Array[Byte]): HttpContext = {
    context = context.result(resultBytes)
    this
  }

  protected def bodyAs[T](clazz: Class[T]): T =
    context.bodyAsClass[T](clazz)

  protected def bodyAs[T](tpe: Type): T =
    context.bodyAsClass[T](tpe)

  def pathParam(name: String): Option[String] =
    Option(context.pathParamMap.get(name))

  def formParam(name: String): Option[String] =
    Option(context.formParam(name))

  def queryParam(name: String): Option[String] =
    Option(context.queryParam(name))

  def header(name: String): Option[String] =
    Option(context.header(name))

  def header(name: String, value: String): HttpContext = {
    context = context.header(name, value)
    this
  }

  def uploadedFile(index: Int): Option[UploadedFile] =
    context.uploadedFiles.toArray.lift(index).map(_.asInstanceOf[JavalinUploadedFile]).map(toUploadedFile)

  def uploadedFile(name: String): Option[UploadedFile] =
    Option(context.uploadedFile(name)).map(toUploadedFile)

  private def toUploadedFile(file: JavalinUploadedFile): UploadedFile = new UploadedFile {
    override def content: InputStream = file.content
    override def filename: String = file.filename
    override def size: Long = file.size
    override def contentType: String = file.contentType
  }

  def contentType(contentType: String): HttpContext = {
    context = context.contentType(contentType)
    this
  }

  def attribute[T](name: String): Option[T] =
    Option(context.attribute[T](name))

  def attribute(name: String, value: Any): HttpContext = {
    context.attribute(name, value)
    this
  }
