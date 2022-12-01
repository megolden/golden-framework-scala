package golden.framework

trait StartupTask(val beforeStart: Boolean = false):
  val order: Int = Int.MaxValue
  def execute(): Unit
