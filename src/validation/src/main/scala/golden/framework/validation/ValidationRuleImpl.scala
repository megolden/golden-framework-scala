package golden.framework.validation

import golden.framework.Predicate

private[validation] class ValidationRuleImpl[T](rule: Predicate[T]) extends ValidationRule[T]:

  private var _failure: T => Exception = _ => ValidationException()
  private var _when: Predicate[T] = _ => true

  override def when(rule: Predicate[T]): ValidationRule[T] = {
    val prev = _when
    _when = value => prev.apply(value) && rule.apply(value)
    this
  }

  override def withMessage(messageProvider: T => String): Unit =
    _failure = value => ValidationException(messageProvider.apply(value).format(value))

  override def withFailure(failure: T => Exception): Unit =
    _failure = failure

  def validate(value: T): Unit = {
    if _when.apply(value) && !rule.apply(value) then
      throw _failure(value)
  }
