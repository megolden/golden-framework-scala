package golden.framework.web

trait RequestHandler:
  def handle(context: HttpContext): Unit
