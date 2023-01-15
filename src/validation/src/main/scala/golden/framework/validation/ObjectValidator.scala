package golden.framework.validation

abstract class ObjectValidator[T](isOption: Boolean = false) extends ValidatorImpl[T](skipNone = isOption)

inline def validatorFor[T](setup: Validator[T] => ?): Validator[T] = {
  val validator = new ValidatorImpl[T]
  setup.apply(validator)
  validator
}

inline def validatorForOption[T](setup: Validator[T] => ?): Validator[T] = {
  val validator = new ValidatorImpl[T](skipNone = true)
  setup.apply(validator)
  validator
}
