package golden.framework

inline def default[A]: A =
  Macros.getDefault[A]

def nullAs[T]: T =
  null.asInstanceOf[T]

inline def nameOf[A](inline expression: A): String =
  Macros.getFullNameOf[A](expression).split('.').last

inline def fullNameOf[A](inline expression: A): String =
  Macros.getFullNameOf[A](expression)

extension (value: Any)

  def isNull: Boolean =
    java.util.Objects.isNull(value)

  def nonNull: Boolean =
    !value.isNull

  def isNone: Boolean = value match
    case option: Option[?] => option.isEmpty
    case _ => false

  def nonNone: Boolean =
    !value.isNone

  def unwrapOption: Any = value match
    case option: Option[?] => option.orNull
    case _ => value

  def isOption: Boolean = value match
    case _: Option[?] => true
    case _ => false

  def isNullOrNone: Boolean =
    value.isNull || value.isNone

  def nonNullOrNone: Boolean =
    value.nonNull && value.nonNone
