package golden.framework.validation.annotations

import golden.framework.validation.{PropertyValidationRule, PropertyValidator}
import golden.framework.OptionExtensions.tap
import scala.annotation.{compileTimeOnly, StaticAnnotation}

@compileTimeOnly("`ValidationAnnotation` is compile time only annotation")
trait ValidationAnnotation extends StaticAnnotation:

  val message: Option[String] = None

  protected def applyRule[P, T](validator: PropertyValidator[P, T]): PropertyValidationRule[P, T]

  final def apply[P, T](validator: PropertyValidator[P, T]): Unit = {
    val rule = applyRule(validator)
    message.tap(rule.withMessage(_))
  }
