package golden.framework.validation.annotations

import golden.framework.validation.rules.CommonValidators.mustBe
import golden.framework.validation.{PropertyValidationRule, PropertyValidator}

class positive private(_message: Option[String]) extends ValidationAnnotation:

  def this(message: String) = this(Some(message))

  def this() = this(None)

  override val message: Option[String] = _message

  override protected def applyRule[P, T](validator: PropertyValidator[P, T]): PropertyValidationRule[P, T] =
    validator.mustBe.greaterThan((value, _) => zeroByTypeOf(value).asInstanceOf[P])

  private def zeroByTypeOf(value: Any): Any = value match {
    case _: Byte => 0:Byte
    case _: Short => 0:Short
    case _: Int => 0
    case _: Long => 0L
    case _: Float => 0F
    case _: Double => 0D
    case _: BigInt => 0:BigInt
    case _: BigDecimal => 0:BigDecimal
    case _: java.lang.Byte => java.lang.Byte.valueOf(0:Byte)
    case _: java.lang.Short => java.lang.Short.valueOf(0:Short)
    case _: java.lang.Integer => java.lang.Integer.valueOf(0)
    case _: java.lang.Long => java.lang.Long.valueOf(0L)
    case _: java.lang.Float => java.lang.Float.valueOf(0F)
    case _: java.lang.Double => java.lang.Double.valueOf(0D)
    case _: java.math.BigInteger => java.math.BigInteger.valueOf(0L)
    case _: java.math.BigDecimal => java.math.BigDecimal.valueOf(0D)
    case other =>
      throw new UnsupportedOperationException(s"data type ${other.getClass} not supported")
  }
