package golden.framework

object StringUtils:
  val Empty: String = ""

  extension (s: String)
    def isEmpty: Boolean = s == null || s.length == 0
    def nonEmpty: Boolean = s != null && s.length != 0
    def isBlank: Boolean = s == null || s.length == 0 || s.forall(_.isWhitespace)
    def nonBlank: Boolean = s != null && s.length > 0 && s.exists(!_.isWhitespace)
