package golden.framework.validation.rules

import golden.framework.validation.{ValidationRule, Validator}
import golden.framework.{nonNullOrNone, Predicate}

private[validation] class CommonValidatorImpl[T](validator: Validator[T], negate: Boolean = false)
  extends CommonValidator[T]:

  override def required: ValidationRule[T] = {
    makeRule(
      _.nonNullOrNone,
      message = "must not be empty.",
      negateMessage = "must be empty.",
      ignoreNullAndNone = false)
  }

  override def equal(value: => T): ValidationRule[T] = {
    makeRule(
      _.equals(value),
      message = s"must be equal to $value.",
      negateMessage = s"must not be equal to $value.")
  }

  override def greaterThan(value: => T): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[Comparable[T]].compareTo(value) > 0,
      message = s"%s must be greater than $value.",
      negateMessage = s"%s must be less than or equal to $value.")
  }

  override def greaterThanOrEqual(value: => T): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[Comparable[T]].compareTo(value) >= 0,
      message = s"%s must be greater than or equal to $value.",
      negateMessage = s"%s must be less than $value.")
  }

  override def lessThan(value: => T): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[Comparable[T]].compareTo(value) < 0,
      message = s"%s must be less than $value.",
      negateMessage = s"%s must be greater than or equal to $value.")
  }

  override def lessThanOrEqual(value: => T): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[Comparable[T]].compareTo(value) <= 0,
      message = s"%s must be less than or equal to $value.",
      negateMessage = s"%s must be greater than $value.")
  }

  override def inclusiveBetween(from: => T, to: => T): ValidationRule[T] = {
    makeRule(
      rawValue => {
        val value = rawValue.asInstanceOf[Comparable[T]]
        value.compareTo(from) >= 0 && value.compareTo(to) <= 0
      },
      message = s"%s must be between $from and $to.",
      negateMessage = s"%s must not be between $from and $to.")
  }

  override def exclusiveBetween(from: => T, to: => T): ValidationRule[T] = {
    makeRule(
      rawValue => {
        val value = rawValue.asInstanceOf[Comparable[T]]
        value.compareTo(from) > 0 && value.compareTo(to) < 0
      },
      message = s"%s must be between $from and $to (exclusive).",
      negateMessage = s"%s must not be between $from and $to (exclusive).")
  }

  override def blank: ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[String].isBlank,
      message = "must be blank.",
      negateMessage = "must not be blank.")
  }

  override def empty: ValidationRule[T] = {
    makeRule(
      {
        case s: String => s.isEmpty
        case i: Iterable[?] => i.isEmpty
        case _ => throw UnsupportedOperationException()
      },
      message = "must be empty.",
      negateMessage = "must not be empty.")
  }

  override def digits: ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[String].forall(_.isDigit),
      message = "%s must be contain digits only.",
      negateMessage = "%s must not be contain digits only.")
  }

  override def matches(pattern: String): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[String].matches('^' + pattern + '$'),
      message = "%s is not in the correct format.",
      negateMessage = "%s is not in the correct format.")
  }

  override def size(size: => Int): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[Iterable[?]].size == size,
      message = s"%s size must be $size.",
      negateMessage = s"%s size must not be $size.")
  }

  override def size(min: => Int, max: => Int): ValidationRule[T] = {
    makeRule(
      rawValue => {
        val value = rawValue.asInstanceOf[Iterable[?]]
        value.size >= min && value.size <= max
      },
      message = s"%s size must be between $min and $max.",
      negateMessage = s"%s size must not be between $min and $max.")
  }

  override def minSize(size: => Int): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[Iterable[?]].size >= size,
      message = s"%s size must be at least $size.",
      negateMessage = s"%s size must be $size or fewer.")
  }

  override def maxSize(size: => Int): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[Iterable[?]].size <= size,
      message = s"%s size must be $size or fewer.",
      negateMessage = s"%s size must be at least $size.")
  }

  override def length(length: => Int): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[String].length == length,
      message = s"%s length must be $length.",
      negateMessage = s"%s length must not be $length.")
  }

  override def length(min: => Int, max: => Int): ValidationRule[T] = {
    makeRule(
      rawValue => {
        val value = rawValue.asInstanceOf[String]
        value.length >= min && value.length <= max
      },
      message = s"%s length must be between $min and $max.",
      negateMessage = s"%s length must not be between $min and $max.")
  }

  override def minLength(length: => Int): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[String].length >= length,
      message = s"%s length must be at least $length.",
      negateMessage = s"%s length must be $length or fewer.")
  }

  override def maxLength(length: => Int): ValidationRule[T] = {
    makeRule(
      _.asInstanceOf[String].length <= length,
      message = s"%s length must be $length or fewer.",
      negateMessage = s"%s length must be at least $length.")
  }

  private def makeRule(
    predicate: Predicate[T],
    message: String,
    negateMessage: String,
    ignoreNullAndNone: Boolean = true): ValidationRule[T] = {

    val rule = if negate then validator.mustNot(predicate) else validator.must(predicate)
    if ignoreNullAndNone then rule.when(_.nonNullOrNone)
    rule.withMessage(if negate then negateMessage else message)
    rule
  }
