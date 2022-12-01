package golden.framework

trait Environment:
  val isDevelopment: Boolean
  val isProduction: Boolean
  val commandLineArgs: Seq[String]
  val currentDir: String
