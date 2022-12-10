package golden.framework.validation

import golden.framework.{Predicate, Predicate2}

private[validation] class PropertyValidationRuleImpl[P, T](rule: Predicate2[P, T])
  extends PropertyValidationRule[P, T] :

  private var _failure: (P, T) => Exception = (_, _) => ValidationException()
  private var _when: Predicate2[P, T] = (_, _) => true

  override def when(rule: Predicate2[P, T]): PropertyValidationRule[P, T] = {
    val prev = _when
    _when = (value, obj) => prev.apply(value, obj) && rule.apply(value, obj)
    this
  }

  override def withMessage(message: String): Unit =
    _failure = (value, obj) => ValidationException(message.format(value, obj))

  override def withFailure(failure: (P, T) => Exception): Unit =
    _failure = failure

  def validate(value: P, obj: T): Unit = {
    if _when.apply(value, obj) && !rule.apply(value, obj) then
      throw _failure(value, obj)
  }
