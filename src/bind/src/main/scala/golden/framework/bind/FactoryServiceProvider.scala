package golden.framework.bind

import golden.framework.TypeInfo

class FactoryServiceProvider(tpe: TypeInfo, factory: Container => Any) extends ServiceProvider:
  override val implementationType: TypeInfo = tpe
  override def get(injector: Container): Any = factory(injector)
