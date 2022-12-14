package golden.framework.validation.rules

import golden.framework.validation.{PropertyValidator, Validator}

object CommonValidators:

  extension[T] (validator: Validator[T])

    def mustBe: CommonValidator[T] =
      new CommonValidatorImpl[T](validator, negate = false)

    def mustNotBe: CommonValidator[T] =
      new CommonValidatorImpl[T](validator, negate = true)

  end extension

  extension[P, T] (validator: PropertyValidator[P, T])

    def mustBe: CommonPropertyValidator[P, T] =
      new CommonPropertyValidatorImpl[P, T](validator, negate = false)

    def mustNotBe: CommonPropertyValidator[P, T] =
      new CommonPropertyValidatorImpl[P, T](validator, negate = true)

  end extension
