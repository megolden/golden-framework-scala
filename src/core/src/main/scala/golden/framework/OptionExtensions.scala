package golden.framework

import java.util.Optional

object OptionExtensions:

  extension [T](optional: Optional[T])

    def toOption: Option[T] = if optional.isPresent then Some(optional.get) else None

  extension [T](option: Option[T])

    def tap[U](f: T => U): Option[T] =
      option.tapEach(f).headOption

    def toOptional: Optional[T] =
      if option.nonEmpty then Optional.of(option.get) else Optional.empty[T]
