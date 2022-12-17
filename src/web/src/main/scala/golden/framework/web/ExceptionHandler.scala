package golden.framework.web

trait ExceptionHandler:
  def handle(exception: Exception, context: HttpContext): Unit
