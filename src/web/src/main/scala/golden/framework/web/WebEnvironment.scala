package golden.framework.web

import golden.framework.Environment

trait WebEnvironment extends Environment:
  val webRootDir: Option[String]
  val isSPAFront: Option[Boolean]
