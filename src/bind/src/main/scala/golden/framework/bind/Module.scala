package golden.framework.bind

trait Module:
  def load(builder: ContainerBuilder): Unit
