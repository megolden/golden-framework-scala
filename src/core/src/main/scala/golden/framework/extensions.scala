package golden.framework

import BooleanUtils.{and, or, not}

inline def default[A]: A =
  Macros.getDefault[A]

def nullAs[T]: T =
  null.asInstanceOf[T]

inline def nameOf[A](inline expression: A => ?): String =
  Macros.getNameOf[A](expression)

extension (value: Any)

  def isNull: Boolean =
    java.util.Objects.isNull(value)

  def nonNull: Boolean =
    not(value.isNull)

  def isNone: Boolean = value match
    case option: Option[?] => option.isEmpty
    case _ => false

  def nonNone: Boolean =
    not(value.isNone)

  def unwrapOption: Any = value match
    case option: Option[?] => option.orNull
    case _ => value

  def isNullOrNone: Boolean =
    value.isNull or value.isNone

  def nonNullOrNone: Boolean =
    value.nonNull and value.nonNone
