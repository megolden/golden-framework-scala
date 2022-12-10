package golden.framework.validation

import golden.framework.{Predicate, Predicate2}

trait PropertyValidator[P, T]:

  def must(rule: Predicate2[P, T]): PropertyValidationRule[P, T]

  final def mustNot(rule: Predicate2[P, T]): PropertyValidationRule[P, T] =
    must(!rule.apply(_, _))

  final def must(rule: Predicate[P]): PropertyValidationRule[P, T] =
    must((value, _) => rule.apply(value))

  def hasValidator(validator: Validator[P]): Unit
