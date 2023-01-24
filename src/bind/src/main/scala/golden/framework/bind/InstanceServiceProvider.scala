package golden.framework.bind

import golden.framework.Type

class InstanceServiceProvider(tpe: Type, instance: Any) extends ServiceProvider:
  override def implementationType: Type = tpe
  def get(): Any = instance
  override final def get(injector: Container): Any = get()
