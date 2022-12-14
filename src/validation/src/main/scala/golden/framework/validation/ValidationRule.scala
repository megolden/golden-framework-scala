package golden.framework.validation

import golden.framework.Predicate

trait ValidationRule[T]:

  def when(rule: Predicate[T]): ValidationRule[T]

  def withMessage(messageProvider: T => String): Unit

  final def withMessage(message: => String): Unit =
    withMessage(_ => message)

  def withFailure(failure: T => Exception): Unit
