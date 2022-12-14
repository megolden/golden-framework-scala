package golden.framework.validation.annotations

import golden.framework.validation.{PropertyValidationRule, PropertyValidator}
import golden.framework.OptionExtensions.tap

trait ValidationAnnotation extends scala.annotation.StaticAnnotation:

  val message: Option[String] = None

  protected def applyRule[P, T](validator: PropertyValidator[P, T]): PropertyValidationRule[P, T]

  final def apply[P, T](validator: PropertyValidator[P, T]): Unit = {
    val rule = applyRule(validator)
    message.tap(rule.withMessage(_))
  }
