package golden.framework

import org.apache.commons.lang3.StringUtils.{isBlank, isEmpty, EMPTY}
import BooleanUtils.not

object StringUtils:
  val Empty: String = EMPTY

  extension (s: String)
    def nonBlank: Boolean = not(isBlank(s))
    def isNullOrBlank: Boolean = isBlank(s)
    def isNullOrEmpty: Boolean = isEmpty(s)
