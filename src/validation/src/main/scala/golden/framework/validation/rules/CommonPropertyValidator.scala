package golden.framework.validation.rules

import golden.framework.validation.PropertyValidationRule

trait CommonPropertyValidator[P, T]:

  def required: PropertyValidationRule[P, T]

  def equal(provider: T => P): PropertyValidationRule[P, T]

  final def equal(value: => P): PropertyValidationRule[P, T] =
    equal(_ => value)

  def greaterThan(provider: (P, T) => P): PropertyValidationRule[P, T]

  final def greaterThan(provider: T => P): PropertyValidationRule[P, T] =
    greaterThan((_, obj) => provider.apply(obj))

  final def greaterThan(value: => P): PropertyValidationRule[P, T] =
    greaterThan((_, _) => value)

  def greaterThanOrEqual(provider: T => P): PropertyValidationRule[P, T]

  final def greaterThanOrEqual(value: => P): PropertyValidationRule[P, T] =
    greaterThanOrEqual(_ => value)

  def lessThan(provider: (P, T) => P): PropertyValidationRule[P, T]

  final def lessThan(provider: T => P): PropertyValidationRule[P, T] =
    lessThan((_, obj) => provider.apply(obj))

  final def lessThan(value: => P): PropertyValidationRule[P, T] =
    lessThan((_, _) => value)

  def lessThanOrEqual(provider: T => P): PropertyValidationRule[P, T]

  final def lessThanOrEqual(value: => P): PropertyValidationRule[P, T] =
    lessThanOrEqual(_ => value)

  def inclusiveBetween(from: T => P, to: T => P): PropertyValidationRule[P, T]

  final def inclusiveBetween(from: => P, to: => P): PropertyValidationRule[P, T] =
    inclusiveBetween(_ => from, _ => to)

  def exclusiveBetween(from: T => P, to: T => P): PropertyValidationRule[P, T]

  final def exclusiveBetween(from: => P, to: => P): PropertyValidationRule[P, T] =
    exclusiveBetween(_ => from, _ => to)

  def blank: PropertyValidationRule[P, T]

  def empty: PropertyValidationRule[P, T]

  def digits: PropertyValidationRule[P, T]

  def matches(pattern: String): PropertyValidationRule[P, T]

  def size(size: T => Int): PropertyValidationRule[P, T]

  final def size(size: => Int): PropertyValidationRule[P, T] =
    this.size(_ => size)

  def size(min: T => Int, max: T => Int): PropertyValidationRule[P, T]

  final def size(min: => Int, max: => Int): PropertyValidationRule[P, T] =
    size(_ => min, _ => max)

  def minSize(size: T => Int): PropertyValidationRule[P, T]

  final def minSize(size: => Int): PropertyValidationRule[P, T] =
    minSize(_ => size)

  def maxSize(size: T => Int): PropertyValidationRule[P, T]

  final def maxSize(size: => Int): PropertyValidationRule[P, T] =
    maxSize(_ => size)

  def length(length: T => Int): PropertyValidationRule[P, T]

  final def length(length: => Int): PropertyValidationRule[P, T] =
    this.length(_ => length)

  def length(min: T => Int, max: T => Int): PropertyValidationRule[P, T]

  final def length(min: => Int, max: => Int): PropertyValidationRule[P, T] =
    length(_ => min, _ => max)

  def minLength(length: T => Int): PropertyValidationRule[P, T]

  final def minLength(length: => Int): PropertyValidationRule[P, T] =
    minLength(_ => length)

  def maxLength(length: T => Int): PropertyValidationRule[P, T]

  final def maxLength(length: => Int): PropertyValidationRule[P, T] =
    maxLength(_ => length)
