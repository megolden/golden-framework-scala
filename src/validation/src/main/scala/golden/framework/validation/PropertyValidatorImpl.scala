package golden.framework.validation

import org.apache.commons.lang3.reflect.FieldUtils.getField
import org.apache.commons.lang3.reflect.MethodUtils.invokeMethod
import scala.collection.mutable.ArrayBuffer as MutableArray
import golden.framework.{Predicate2, isNull, isNone, isOption, unwrapOption}
import golden.framework.OptionExtensions.tap

private class PropertyValidatorImpl[P, T](propertyPath: String, skipNone: Boolean = false)
  extends PropertyValidator[P, T]:

  private val _rules = MutableArray.empty[PropertyValidationRuleImpl[P, T]]
  private val _validators = MutableArray.empty[Validator[P]]

  override def must(rule: Predicate2[P, T]): PropertyValidationRule[P, T] = {
    val validationRule = new PropertyValidationRuleImpl[P, T](rule, propertyPath)
    validationRule.withMessage("the specified condition was not met for %3$s.")
    _rules += validationRule
    validationRule
  }

  override def hasValidator(validator: Validator[P]): Unit =
    _validators += validator

  def validate(obj: Any): Unit = {
    if _rules.isEmpty && _validators.isEmpty then return

    val rawValue = getPropertyValue(propertyPath, obj)

    if skipNone && rawValue.isNone then return

    val valueOrUnwrapped =
      if skipNone && rawValue.isOption
      then rawValue.unwrapOption.asInstanceOf[P]
      else rawValue.asInstanceOf[P]

    _rules.foreach(_.validate(valueOrUnwrapped, obj.asInstanceOf[T]))

    _validators.foreach(_.validate(valueOrUnwrapped))
  }

  private def getPropertyValue(path: String, obj: Any): Any = {
    if obj.isNull || path.isEmpty then
      return obj

    val clazz = obj.getClass
    val nameSepIndex = path.indexOf('.')
    val name = if nameSepIndex >= 0 then path.take(nameSepIndex) else path
    val value = Option(getField(clazz, name, true)).map(_.get(obj)).getOrElse(invokeMethod(obj, true, name))
    if nameSepIndex >= 0
    then getPropertyValue(path.drop(nameSepIndex + 1), value)
    else value
  }
