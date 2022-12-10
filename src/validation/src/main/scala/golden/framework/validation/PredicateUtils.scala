package golden.framework.validation

import golden.framework.{Predicate, Predicate2}

object PredicateUtils:

  def not[T](predicate: Predicate[T]): Predicate[T] =
    value => !predicate.apply(value)

  extension [T](predicate: Predicate[T])
    def and(predicate2: Predicate[T]): Predicate[T] =
      value => predicate.apply(value) && predicate2.apply(value)

    def andNot(predicate2: Predicate[T]): Predicate[T] =
      value => predicate.apply(value) && !predicate2.apply(value)

    def or(predicate2: Predicate[T]): Predicate[T] =
      value => predicate.apply(value) || predicate2.apply(value)

    def orNot(predicate2: Predicate[T]): Predicate[T] =
      value => predicate.apply(value) || !predicate2.apply(value)
