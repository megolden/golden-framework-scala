package golden.framework

object BooleanUtils:

  /** consider the [[https://en.wikipedia.org/wiki/Short-circuit_evaluation Short-circuit evaluation]] */
  inline def not(b: Boolean): Boolean = !b

  extension (b: Boolean)

    /** consider the [[https://en.wikipedia.org/wiki/Short-circuit_evaluation Short-circuit evaluation]] */
    inline def and(c: Boolean): Boolean = b && c

    /** consider the [[https://en.wikipedia.org/wiki/Short-circuit_evaluation Short-circuit evaluation]] */
    inline def or(c: Boolean): Boolean = b || c
