package golden.framework

object CommonExtensions:

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

  end extension
