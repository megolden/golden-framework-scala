package golden

package object framework:

  type Predicate[T] = T => Boolean

  type Predicate2[A, B] = (A, B) => Boolean

  inline def default[T]: T = null.asInstanceOf[T]

  inline def typeOf[T]: Type = Type.of[T]

  import scala.quoted.*

  inline def fullNameOf(inline expression: Any): String =
    ${ Macros.fullNameOf('{expression}) }

  inline def nameOf(inline expression: Any): String =
    ${ Macros.nameOf('{ expression }) }
