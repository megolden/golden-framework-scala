package golden.framework.bind

import golden.framework.Type

class FactoryServiceProvider(tpe: Type, factory: Container => Any) extends ServiceProvider:
  override def implementationType: Type = tpe
  override def get(injector: Container): Any = factory(injector)
