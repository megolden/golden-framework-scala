package golden.framework.bind

import golden.framework.TypeInfo

class InstanceServiceProvider(tpe: TypeInfo, instance: Any) extends ServiceProvider:
  override val implementationType: TypeInfo = tpe
  def get(): Any = instance
  override final def get(injector: Container): Any = get()
