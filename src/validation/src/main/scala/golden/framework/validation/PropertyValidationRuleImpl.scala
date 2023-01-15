package golden.framework.validation

import golden.framework.{Predicate, Predicate2}

private class PropertyValidationRuleImpl[P, T](rule: Predicate2[P, T], propertyPath: String)
  extends PropertyValidationRule[P, T]:

  private var _failure: (P, T) => Exception = (_, _) => ValidationException()
  private var _when: Predicate2[P, T] = (_, _) => true

  override def when(rule: Predicate2[P, T]): PropertyValidationRule[P, T] = {
    val prev = _when
    _when = (value, obj) => prev.apply(value, obj) && rule.apply(value, obj)
    this
  }

  override def withMessage(messageProvider: (P, T) => String): Unit =
    _failure = (value, obj) => {
      ValidationException(
        messageProvider.apply(value, obj)
        .format(value, obj, propertyPath.split('.').last))
    }

  override def withFailure(failure: (P, T) => Exception): Unit =
    _failure = failure

  def validate(value: P, obj: T): Unit = {
    if _when.apply(value, obj) && !rule.apply(value, obj) then
      throw _failure(value, obj)
  }
