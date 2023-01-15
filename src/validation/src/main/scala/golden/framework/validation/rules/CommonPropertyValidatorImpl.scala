package golden.framework.validation.rules

import golden.framework.validation.{PropertyValidationRule, PropertyValidator}
import golden.framework.{nonNullOrNone, Predicate, Predicate2}

private class CommonPropertyValidatorImpl[P, T](validator: PropertyValidator[P, T], negate: Boolean = false)
  extends CommonPropertyValidator[P, T]:

  override def required: PropertyValidationRule[P, T] = {
    makeRule(
      (value, _) => value.nonNullOrNone,
      message = (_, _) => "%3$s must not be empty.",
      negateMessage = (_, _) => "%3$s must be empty.",
      ignoreNullAndNone = false)
  }

  override def equal(provider: T => P): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.equals(provider.apply(obj)),
      message = (_, obj) => s"%3$$s must be equal to ${provider.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s must not be equal to ${provider.apply(obj)}.")
  }

  override def greaterThan(provider: (P, T) => P): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[Comparable[P]].compareTo(provider.apply(value, obj)) > 0,
      message = (value, obj) => s"%3$$s must be greater than ${provider.apply(value, obj)}.",
      negateMessage = (value, obj) => s"%3$$s must be less than or equal to ${provider.apply(value, obj)}.")
  }

  override def greaterThanOrEqual(provider: T => P): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[Comparable[P]].compareTo(provider.apply(obj)) >= 0,
      message = (_, obj) => s"%3$$s must be greater than or equal to ${provider.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s must be less than ${provider.apply(obj)}.")
  }

  override def lessThan(provider: (P, T) => P): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[Comparable[P]].compareTo(provider.apply(value, obj)) < 0,
      message = (value, obj) => s"%3$$s must be less than ${provider.apply(value, obj)}.",
      negateMessage = (value, obj) => s"%3$$s must be greater than or equal to ${provider.apply(value, obj)}.")
  }

  override def lessThanOrEqual(provider: T => P): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[Comparable[P]].compareTo(provider.apply(obj)) <= 0,
      message = (_, obj) => s"%3$$s must be less than or equal to ${provider.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s must be greater than ${provider.apply(obj)}.")
  }

  override def inclusiveBetween(from: T => P, to: T => P): PropertyValidationRule[P, T] = {
    makeRule(
      (rawValue, obj) => {
        val value = rawValue.asInstanceOf[Comparable[P]]
        value.compareTo(from.apply(obj)) >= 0 && value.compareTo(to.apply(obj)) <= 0
      },
      message = (_, obj) => s"%3$$s must be between ${from.apply(obj)} and ${to.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s must not be between ${from.apply(obj)} and ${to.apply(obj)}.")
  }

  override def exclusiveBetween(from: T => P, to: T => P): PropertyValidationRule[P, T] = {
    makeRule(
      (rawValue, obj) => {
        val value = rawValue.asInstanceOf[Comparable[P]]
        value.compareTo(from.apply(obj)) > 0 && value.compareTo(to.apply(obj)) < 0
      },
      message = (_, obj) => s"%3$$s must be between ${from.apply(obj)} and ${to.apply(obj)} (exclusive).",
      negateMessage = (_, obj) => s"%3$$s must not be between ${from.apply(obj)} and ${to.apply(obj)} (exclusive).")
  }

  override def blank: PropertyValidationRule[P, T] = {
    makeRule(
      (value, _) => value.asInstanceOf[String].isBlank,
      message = (_, _) => "%3$s must be blank.",
      negateMessage = (_, _) => "%3$s must not be blank.")
  }

  override def empty: PropertyValidationRule[P, T] = {
    makeRule(
      (value, _) => value match {
        case s: String => s.isEmpty
        case i: Iterable[?] => i.isEmpty
        case _ => throw UnsupportedOperationException()
      },
      message = (_, _) => "%3$s must be empty.",
      negateMessage = (_, _) => "%3$s must not be empty.")
  }

  override def digits: PropertyValidationRule[P, T] = {
    makeRule(
      (value, _) => value.asInstanceOf[String].forall(_.isDigit),
      message = (_, _) => "%3$s must be contain digits only.",
      negateMessage = (_, _) => "%3$s must not be contain digits only.")
  }

  override def matches(pattern: String): PropertyValidationRule[P, T] = {
    makeRule(
      (value, _) => value.asInstanceOf[String].matches('^' + pattern + '$'),
      message = (_, _) => "%3$s value is not in the correct format.",
      negateMessage = (_, _) => "%3$s value is not in the correct format.")
  }

  override def size(size: T => Int): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[Iterable[?]].size == size.apply(obj),
      message = (_, obj) => s"%3$$s size must be ${size.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s size must not be ${size.apply(obj)}.")
  }

  override def size(min: T => Int, max: T => Int): PropertyValidationRule[P, T] = {
    makeRule(
      (rawValue, obj) => {
        val value = rawValue.asInstanceOf[Iterable[?]]
        value.size >= min.apply(obj) && value.size <= max.apply(obj)
      },
      message = (_, obj) => s"%3$$s size must be between ${min.apply(obj)} and ${max.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s size must not be between ${min.apply(obj)} and ${max.apply(obj)}.")
  }

  override def minSize(size: T => Int): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[Iterable[?]].size >= size.apply(obj),
      message = (_, obj) => s"%3$$s size must be at least ${size.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s size must be ${size.apply(obj)} or fewer.")
  }

  override def maxSize(size: T => Int): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[Iterable[?]].size <= size.apply(obj),
      message = (_, obj) => s"%3$$s size must be ${size.apply(obj)} or fewer.",
      negateMessage = (_, obj) => s"%3$$s size must be at least ${size.apply(obj)}.")
  }

  override def length(length: T => Int): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[String].length == length.apply(obj),
      message = (_, obj) => s"%3$$s length must be ${length.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s length must not be ${length.apply(obj)}.")
  }

  override def length(min: T => Int, max: T => Int): PropertyValidationRule[P, T] = {
    makeRule(
      (rawValue, obj) => {
        val value = rawValue.asInstanceOf[String]
        value.length >= min.apply(obj) && value.length <= max.apply(obj)
      },
      message = (_, obj) => s"%3$$s length must be between ${min.apply(obj)} and ${max.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s length must not be between ${min.apply(obj)} and ${max.apply(obj)}.")
  }

  override def minLength(length: T => Int): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[String].length >= length.apply(obj),
      message = (_, obj) => s"%3$$s length must be at least ${length.apply(obj)}.",
      negateMessage = (_, obj) => s"%3$$s length must be ${length.apply(obj)} or fewer.")
  }

  override def maxLength(length: T => Int): PropertyValidationRule[P, T] = {
    makeRule(
      (value, obj) => value.asInstanceOf[String].length <= length.apply(obj),
      message = (_, obj) => s"%3$$s length must be ${length.apply(obj)} or fewer.",
      negateMessage = (_, obj) => s"%3$$s length must be at least ${length.apply(obj)}.")
  }

  private def makeRule(
    predicate: Predicate2[P, T],
    message: (P, T) => String,
    negateMessage: (P, T) => String,
    ignoreNullAndNone: Boolean = true): PropertyValidationRule[P, T] = {

    val rule = if negate then validator.mustNot(predicate) else validator.must(predicate)
    if ignoreNullAndNone then rule.when(_.nonNullOrNone)
    rule.withMessage(if negate then negateMessage else message)
    rule
  }
