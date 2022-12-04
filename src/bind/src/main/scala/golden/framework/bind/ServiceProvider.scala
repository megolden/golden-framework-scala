package golden.framework.bind

import golden.framework.TypeInfo

trait ServiceProvider:
  val implementationType: TypeInfo
  def get(injector: Container): Any
