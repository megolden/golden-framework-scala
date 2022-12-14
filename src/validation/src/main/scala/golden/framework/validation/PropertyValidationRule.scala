package golden.framework.validation

import golden.framework.{Predicate, Predicate2}

trait PropertyValidationRule[P, T]:

  def when(rule: Predicate2[P, T]): PropertyValidationRule[P, T]

  final def when(rule: Predicate[P]): PropertyValidationRule[P, T] =
    when((value, _) => rule.apply(value))

  def withMessage(messageProvider: (P, T) => String): Unit

  final def withMessage(message: => String): Unit =
    withMessage((_, _) => message)

  def withFailure(failure: (P, T) => Exception): Unit

  final def withFailure(failure: P => Exception): Unit =
    withFailure((value, _) => failure.apply(value))
