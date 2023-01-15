package golden.framework.bind

import golden.framework.Type
import ServiceLifetime.Transient
import annotation.StaticAnnotation

class service private(val as: Option[Type], val lifetime: ServiceLifetime) extends StaticAnnotation:
  def this() = this(None, Transient)
  def this(as: Type, lifetime: ServiceLifetime = Transient) = this(Some(as), lifetime)
  def this(lifetime: ServiceLifetime) = this(None, lifetime)
