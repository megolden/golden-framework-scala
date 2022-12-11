package golden.framework.validation.annotations

import golden.framework.validation.PropertyValidator

trait ValidationAnnotation extends scala.annotation.StaticAnnotation :
  def applyRule[P, T](validator: PropertyValidator[P, T]): Unit
