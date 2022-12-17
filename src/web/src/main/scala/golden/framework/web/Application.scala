package golden.framework.web

trait Application:

  private[web] def start(host: Option[String], port: Option[Int]): Application

  final def start(host: String, port: Int): Application = {
    start(Some(host), Some(port))
    this
  }

  final def start(port: Int): Application = {
    start(None, Some(port))
    this
  }

  final def start(): Application = {
    start(None, None)
    this
  }

  def stop(): Application
