package golden.framework.web

private trait HttpMethodAnnotation extends scala.annotation.StaticAnnotation:
  val path: String
  val order: Int
