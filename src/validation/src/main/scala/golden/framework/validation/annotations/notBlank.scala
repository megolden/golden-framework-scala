package golden.framework.validation.annotations

import golden.framework.validation.{PropertyValidationRule, PropertyValidator}
import golden.framework.validation.rules.CommonValidators.mustNotBe

class notBlank private(_message: Option[String]) extends ValidationAnnotation:

  def this(message: String) = this(Some(message))

  def this() = this(None)

  override val message: Option[String] = _message

  override protected def applyRule[P, T](validator: PropertyValidator[P, T]): PropertyValidationRule[P, T] =
    validator.mustNotBe.blank
