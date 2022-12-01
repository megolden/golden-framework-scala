package golden.framework

object BooleanUtils:
  inline def not(b: Boolean): Boolean = !b
  extension (b: Boolean)
    inline def and(c: Boolean): Boolean = b && c
    inline def or(c: Boolean): Boolean = b || c
