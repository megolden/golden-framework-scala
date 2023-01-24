package golden.framework.bind

import golden.framework.Type

trait ServiceProvider:
  def implementationType: Type
  def get(injector: Container): Any
