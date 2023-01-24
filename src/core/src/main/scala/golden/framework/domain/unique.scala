package golden.framework.domain

import scala.annotation.{compileTimeOnly, StaticAnnotation}

@compileTimeOnly("`unique` is compile time only annotation")
class unique extends StaticAnnotation
