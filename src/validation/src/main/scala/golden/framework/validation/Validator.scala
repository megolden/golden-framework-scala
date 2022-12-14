package golden.framework.validation

import golden.framework.{Predicate, fullNameOf}
import golden.framework.validation.annotations.ValidationAnnotation
import golden.framework.ReflectionUtils.getAnnotatedMembers
import golden.framework.TypeInfoUtils.isOptionType

trait Validator[T]:

  def must(rule: Predicate[T]): ValidationRule[T]

  final def mustNot(rule: Predicate[T]): ValidationRule[T] =
    must(!rule.apply(_))

  private[validation] def the[P](propertyPath: String, skipNone: Boolean): PropertyValidator[P, T]

  final def the[P](propertyPath: String): PropertyValidator[P, T] =
    the(propertyPath, skipNone = false)

  final inline def the[P](inline property: T => P): PropertyValidator[P, T] =
    the[P](fullNameOf(property))

  final inline def theOption[P](inline property: T => Option[P]): PropertyValidator[P, T] =
    theOption[P](fullNameOf(property))

  final def theOption[P](propertyPath: String): PropertyValidator[P, T] =
    the[P](propertyPath, skipNone = true)

  def validate(value: Any): Unit

  def hasValidator(validator: Validator[T]): Unit

object Validator:

  inline def validate[T](annotatedObject: T): Unit = {
    val properties = getAnnotatedMembers[T, ValidationAnnotation]
    val validator = validatorFor[T] { validator =>
      properties.foreach { case (name, (tpe, annotations)) =>
        val propertyValidator = validator.the[Any](name, skipNone = tpe.isOptionType)
        annotations.foreach { _.apply[Any, T](propertyValidator) }
      }
    }
    validator.validate(annotatedObject)
  }
