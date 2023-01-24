package golden.framework.bind

import golden.framework.Type
import scala.annotation.{compileTimeOnly, StaticAnnotation}

@compileTimeOnly("`service` is compile time only annotation")
class service private(val as: Option[Type], val asSingleton: Boolean, val asContainerScoped: Boolean)
  extends StaticAnnotation:

  def this(as: Type = null, asSingleton: Boolean = false, asContainerScoped: Boolean = false) =
    this(Option(as), asSingleton, asContainerScoped)
