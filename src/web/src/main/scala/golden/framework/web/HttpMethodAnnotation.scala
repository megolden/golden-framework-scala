package golden.framework.web

private[web] trait HttpMethodAnnotation extends scala.annotation.StaticAnnotation:
  val path: String
  val order: Int
