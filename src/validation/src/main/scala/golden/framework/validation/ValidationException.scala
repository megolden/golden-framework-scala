package golden.framework.validation

final class ValidationException(message: String) extends Exception(message):
  def this() = this("validation failed")
