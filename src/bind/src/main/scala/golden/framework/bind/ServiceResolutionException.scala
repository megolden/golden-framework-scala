package golden.framework.bind

import golden.framework.Type

class ServiceResolutionException(message: String) extends Exception(message):
  def this(tpe: Type) = this(s"no service registered for: $tpe")
