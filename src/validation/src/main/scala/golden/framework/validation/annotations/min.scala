package golden.framework.validation.annotations

import golden.framework.validation.{PropertyValidationRule, PropertyValidator}
import golden.framework.validation.rules.CommonValidators.mustBe

class min private(provider: Any => Any, _message: Option[String]) extends ValidationAnnotation:

  def this(value: => Any, message: String) = this(_ => value, Some(message))

  def this(provider: Any => Any, message: String) = this(provider, Some(message))

  def this(value: => Any) = this(_ => value, None)

  def this(provider: Any => Any) = this(provider, None)

  override val message: Option[String] = _message

  override protected def applyRule[P, T](validator: PropertyValidator[P, T]): PropertyValidationRule[P, T] =
    validator.mustBe.greaterThanOrEqual(obj => provider.apply(obj).asInstanceOf[P])
