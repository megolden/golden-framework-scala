package golden.framework.validation.annotations

import golden.framework.validation.{PropertyValidationRule, PropertyValidator}
import golden.framework.validation.rules.CommonValidators.mustBe
import java.time.*

class future private(utc: Boolean, _message: Option[String]) extends ValidationAnnotation:

  def this(message: String) = this(utc = false, Some(message))

  def this(utc: Boolean) = this(utc, None)

  def this(utc: Boolean, message: String) = this(utc, Some(message))

  def this() = this(utc = false, None)

  override val message: Option[String] = _message

  override protected def applyRule[P, T](validator: PropertyValidator[P, T]): PropertyValidationRule[P, T] =
    validator.mustBe.greaterThan((value, _) => nowByTypeOf(value, utc).asInstanceOf[P])

  private def nowByTypeOf(value: Any, utc: Boolean): Any = value match {
    case _: LocalDateTime =>
      if utc then LocalDateTime.now(ZoneOffset.UTC) else LocalDateTime.now(ZoneId.systemDefault)
    case _: LocalDate =>
      if utc then LocalDate.now(ZoneOffset.UTC) else LocalDate.now(ZoneId.systemDefault)
    case zoned: ZonedDateTime =>
      ZonedDateTime.now(zoned.getZone)
    case offset: OffsetDateTime =>
      OffsetDateTime.now(offset.getOffset)
    case _: Instant =>
      Instant.now
    case _: LocalTime =>
      if utc then LocalTime.now(ZoneOffset.UTC) else LocalTime.now(ZoneId.systemDefault)
    case offset: OffsetTime =>
      OffsetTime.now(offset.getOffset)
    case other =>
      throw new UnsupportedOperationException(s"data type ${other.getClass} not supported")
  }
