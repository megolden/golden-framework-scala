package golden.framework.bind

import golden.framework.TypeInfo
import ServiceLifetime.Transient
import annotation.StaticAnnotation

class service private(val as: Option[TypeInfo], val lifetime: ServiceLifetime) extends StaticAnnotation:
  def this() = this(None, Transient)
  def this(as: TypeInfo, lifetime: ServiceLifetime = Transient) = this(Some(as), lifetime)
  def this(lifetime: ServiceLifetime) = this(None, lifetime)
