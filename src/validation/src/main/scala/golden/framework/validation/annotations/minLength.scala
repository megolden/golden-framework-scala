package golden.framework.validation.annotations

import golden.framework.validation.rules.CommonValidators.mustBe
import golden.framework.validation.{PropertyValidationRule, PropertyValidator}

class minLength private(provider: Any => Int, _message: Option[String]) extends ValidationAnnotation:

  def this(value: => Int, message: String) = this(_ => value, Some(message))

  def this(provider: Any => Int, message: String) = this(provider, Some(message))

  def this(value: => Int) = this(_ => value, None)

  def this(provider: Any => Int) = this(provider, None)

  override val message: Option[String] = _message

  override protected def applyRule[P, T](validator: PropertyValidator[P, T]): PropertyValidationRule[P, T] =
    validator.mustBe.minLength(obj => provider.apply(obj))
