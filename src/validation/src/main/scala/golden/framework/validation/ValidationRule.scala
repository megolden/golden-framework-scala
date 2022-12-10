package golden.framework.validation

import golden.framework.Predicate

trait ValidationRule[T]:
  def when(rule: Predicate[T]): ValidationRule[T]
  def withMessage(message: String): Unit
  def withFailure(failure: T => Exception): Unit
