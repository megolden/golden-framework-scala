package golden.framework.validation

abstract class ObjectValidator[T] extends ValidatorImpl[T]

def validatorFor[T](setup: Validator[T] => ?): Validator[T] = {
  val validator = new ValidatorImpl[T]
  setup.apply(validator)
  validator
}
