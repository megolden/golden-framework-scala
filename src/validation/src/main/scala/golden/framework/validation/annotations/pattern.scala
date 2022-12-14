package golden.framework.validation.annotations

import golden.framework.validation.rules.CommonValidators.mustBe
import golden.framework.validation.{PropertyValidationRule, PropertyValidator}

class pattern private(pattern: String, _message: Option[String]) extends ValidationAnnotation:

  def this(pattern: String, message: String) = this(pattern, Some(message))

  def this(pattern: String) = this(pattern, None)

  override val message: Option[String] = _message

  override protected def applyRule[P, T](validator: PropertyValidator[P, T]): PropertyValidationRule[P, T] =
    validator.mustBe.matches(pattern)
