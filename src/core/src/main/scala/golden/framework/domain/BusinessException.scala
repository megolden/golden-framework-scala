package golden.framework.domain

class BusinessException(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean)
  extends Exception(message, cause, enableSuppression, writableStackTrace):

  def this() =
    this(message = null, cause = null, enableSuppression = false, writableStackTrace = false)

  def this(message: String) =
    this(message, cause = null, enableSuppression = false, writableStackTrace = false)

  def this(message: String, cause: Throwable) =
    this(message, cause, enableSuppression = false, writableStackTrace = false)

  def this(cause: Throwable) =
    this(message = null, cause, enableSuppression = false, writableStackTrace = false)
