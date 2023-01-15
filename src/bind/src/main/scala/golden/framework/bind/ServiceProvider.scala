package golden.framework.bind

import golden.framework.Type

private trait ServiceProvider:
  def implementationType: Type
  def get(injector: Container): Any
