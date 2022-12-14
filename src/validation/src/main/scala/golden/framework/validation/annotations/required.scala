package golden.framework.validation.annotations

import golden.framework.validation.rules.CommonValidators.mustBe
import golden.framework.validation.{PropertyValidationRule, PropertyValidator}

class required private(_message: Option[String]) extends ValidationAnnotation:

  def this(message: String) = this(Some(message))

  def this() = this(None)

  override val message: Option[String] = _message

  override protected def applyRule[P, T](validator: PropertyValidator[P, T]): PropertyValidationRule[P, T] =
    validator.mustBe.required
