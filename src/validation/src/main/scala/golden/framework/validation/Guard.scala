package golden.framework.validation

import golden.framework.default
import golden.framework.CommonExtensions.{nonNullOrNone, nonNull, unwrapOption}
import golden.framework.StringUtils.nonBlank
import java.util.Objects.equals as equalObjects

object Guard:

  def against[T](rule: Boolean, name: String = null, message: String = null, exception: Exception = null): T = {
    validate(
      default[T],
      rule,
      name,
      Option(message).getOrElse("%s has invalid value".format(Option(name).getOrElse("argument"))),
      exception)
  }

  object Against:

    def zero[T](value: T, name: String = null, message: String = null, exception: Exception = null): T = {
      validate(
        value,
        !value.equals(zeroByTypeOf(value)),
        name,
        Option(message).getOrElse("%s cannot be zero".format(Option(name).getOrElse("argument"))),
        exception)
    }

    def negativeOrZero[T](value: T, name: String = null, message: String = null, exception: Exception = null): T = {
      validate(
        value,
        value.asInstanceOf[Comparable[T]].compareTo(zeroByTypeOf(value).asInstanceOf[T]) > 0,
        name,
        Option(message).getOrElse("%s cannot be zero or negative".format(Option(name).getOrElse("argument"))),
        exception)
    }

    def negative[T](value: T, name: String = null, message: String = null, exception: Exception = null): T = {
      validate(
        value,
        value.asInstanceOf[Comparable[T]].compareTo(zeroByTypeOf(value).asInstanceOf[T]) >= 0,
        name,
        Option(message).getOrElse("%s cannot be zero or negative".format(Option(name).getOrElse("argument"))),
        exception)
    }

    def nullOrNone[T](value: T, name: String = null, message: String = null, exception: Exception = null): T = {
      validate(
        value,
        value.nonNullOrNone,
        name,
        Option(message).getOrElse("%s cannot be empty".format(Option(name).getOrElse("argument"))),
        exception)
    }

    def empty[T](value: T, name: String = null, message: String = null, exception: Exception = null): T = {
      validate(
        value,
        value match {
          case s: String => s.nonEmpty
          case i: Iterable[?] => i.nonEmpty
        },
        name,
        Option(message).getOrElse("%s cannot be empty".format(Option(name).getOrElse("argument"))),
        exception)
    }

    def blank[T](value: T, name: String = null, message: String = null, exception: Exception = null): T = {
      validate(
        value,
        value.asInstanceOf[String].nonBlank,
        name,
        Option(message).getOrElse("%s cannot be empty".format(Option(name).getOrElse("argument"))),
        exception)
    }

    def beNull[T](value: T, name: String = null, message: String = null, exception: Exception = null): T = {
      validate(
        value,
        value.nonNull,
        name,
        Option(message).getOrElse("%s cannot be empty".format(Option(name).getOrElse("argument"))),
        exception)
    }

    def equal[T](value: T, other: T, name: String = null, message: String = null, exception: Exception = null): T = {
      validate(
        value,
        equalObjects(value, other),
        name,
        Option(message).getOrElse(s"%s must be equal to $other".format(Option(name).getOrElse("argument"))),
        exception)
    }

    private def zeroByTypeOf(value: Any): Any = value match {
    case _: Byte => 0: Byte
    case _: Short => 0: Short
    case _: Int => 0
    case _: Long => 0L
    case _: Float => 0F
    case _: Double => 0D
    case _: BigInt => 0: BigInt
    case _: BigDecimal => 0: BigDecimal
    case _: java.lang.Byte => java.lang.Byte.valueOf(0: Byte)
    case _: java.lang.Short => java.lang.Short.valueOf(0: Short)
    case _: java.lang.Integer => java.lang.Integer.valueOf(0)
    case _: java.lang.Long => java.lang.Long.valueOf(0L)
    case _: java.lang.Float => java.lang.Float.valueOf(0F)
    case _: java.lang.Double => java.lang.Double.valueOf(0D)
    case _: java.math.BigInteger => java.math.BigInteger.valueOf(0L)
    case _: java.math.BigDecimal => java.math.BigDecimal.valueOf(0D)
    case other =>
      throw new UnsupportedOperationException(s"data type ${other.getClass} not supported")
  }

  private def validate[T](
    value: T,
    rule: Boolean,
    name: String = null,
    message: String = null,
    exception: Exception = null): T = {

    if rule
    then value
    else throw Option(exception).getOrElse(
      IllegalArgumentException(
        Option(message).getOrElse("%s has invalid value: %s".format(Option(name).getOrElse("argument"), value))))
  }
