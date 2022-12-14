package golden.framework.validation.rules

import golden.framework.validation.ValidationRule

trait CommonValidator[T]:

  def required: ValidationRule[T]

  def equal(value: => T): ValidationRule[T]

  def greaterThan(value: => T): ValidationRule[T]

  def greaterThanOrEqual(value: => T): ValidationRule[T]

  def lessThan(value: => T): ValidationRule[T]

  def lessThanOrEqual(value: => T): ValidationRule[T]

  def inclusiveBetween(from: => T, to: => T): ValidationRule[T]

  def exclusiveBetween(from: => T, to: => T): ValidationRule[T]

  def blank: ValidationRule[T]

  def empty: ValidationRule[T]

  def digits: ValidationRule[T]

  def matches(pattern: String): ValidationRule[T]

  def size(size: => Int): ValidationRule[T]

  def size(min: => Int, max: => Int): ValidationRule[T]

  def minSize(size: => Int): ValidationRule[T]

  def maxSize(size: => Int): ValidationRule[T]

  def length(length: => Int): ValidationRule[T]

  def length(min: => Int, max: => Int): ValidationRule[T]

  def minLength(length: => Int): ValidationRule[T]

  def maxLength(length: => Int): ValidationRule[T]
