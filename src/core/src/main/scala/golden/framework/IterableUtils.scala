package golden.framework

object IterableUtils:
  extension [A](iterable: Iterable[A])
    def ifEmpty[U](f: () => U): Iterable[A] = {
      if iterable.isEmpty then f.apply()
      iterable
    }
