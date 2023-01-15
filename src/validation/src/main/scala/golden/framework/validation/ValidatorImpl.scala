package golden.framework.validation

import scala.collection.mutable.ArrayBuffer as MutableArray
import golden.framework.Predicate
import golden.framework.{isNone, isOption, unwrapOption}
import golden.framework.OptionExtensions.tap

private class ValidatorImpl[T](skipNone: Boolean = false) extends Validator[T]:

  private val _rules = MutableArray.empty[Either[ValidationRuleImpl[T], PropertyValidatorImpl[?, T]]]
  private val _validators = MutableArray.empty[Validator[T]]

  override def must(rule: Predicate[T]): ValidationRule[T] = {
    val validationRule = new ValidationRuleImpl[T](rule)
    validationRule.withMessage("the specified condition was not met.")
    _rules += Left(validationRule)
    validationRule
  }

  override def the[P](propertyPath: String, skipNone: Boolean): PropertyValidator[P, T] = {
    val validationRule = new PropertyValidatorImpl[P, T](propertyPath, skipNone)
    _rules += Right(validationRule)
    validationRule
  }

  override def hasValidator(validator: Validator[T]): Unit =
    _validators += validator

  override def validate(value: Any): Unit = {
    if _rules.isEmpty && _validators.isEmpty then return

    if skipNone && value.isNone then return

    val valueOrUnwrapped =
      if skipNone && value.isOption
      then value.unwrapOption.asInstanceOf[T]
      else value.asInstanceOf[T]

    _rules.foreach {
      case Left(rule) => rule.validate(valueOrUnwrapped)
      case Right(validator) => validator.validate(valueOrUnwrapped)
    }

    _validators.foreach(_.validate(valueOrUnwrapped))
  }
