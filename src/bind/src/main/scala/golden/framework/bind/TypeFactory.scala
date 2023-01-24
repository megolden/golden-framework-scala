package golden.framework.bind

import golden.framework.Type

trait TypeFactory:
  val argumentTypes: Seq[Type]
  def newInstance(args: Seq[Any]): Any
