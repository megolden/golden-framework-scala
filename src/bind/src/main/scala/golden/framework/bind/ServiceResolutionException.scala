package golden.framework.bind

import golden.framework.TypeInfo

class ServiceResolutionException(message: String) extends Exception(message):
  def this(tpe: TypeInfo) = this(s"no service registered for '$tpe'")
