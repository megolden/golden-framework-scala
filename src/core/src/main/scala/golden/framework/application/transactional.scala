package golden.framework.application
import scala.annotation.{compileTimeOnly, StaticAnnotation}

@compileTimeOnly("`transactional` is compile time only annotation")
class transactional extends StaticAnnotation
