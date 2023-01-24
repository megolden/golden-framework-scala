package golden.framework.web
import scala.annotation.{compileTimeOnly, StaticAnnotation}

@compileTimeOnly("`HttpMethodAnnotation` is compile time only annotation")
private trait HttpMethodAnnotation extends StaticAnnotation:
  val path: String
  val order: Int
