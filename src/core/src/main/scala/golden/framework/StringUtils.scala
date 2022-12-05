package golden.framework

object StringUtils:
  val Empty: String = ""

  extension (s: String)
    def isEmpty: Boolean = s == null || s.isEmpty
    def nonEmpty: Boolean = !isEmpty
    def isBlank: Boolean = s == null || s.isBlank
    def nonBlank: Boolean = !isBlank
