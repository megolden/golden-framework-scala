package golden.framework.application

import golden.framework.{TypeInfo, typeOf}

trait CommandDispatcher:

  inline final def dispatch[TCommand <: Command[TResponse], TResponse](command: TCommand): TResponse =
    dispatch[TCommand, TResponse](command, typeOf[TCommand], typeOf[TResponse])

  protected def dispatch[TCommand <: Command[TResponse], TResponse]
    (command: TCommand, commandType: TypeInfo, responseType: TypeInfo): TResponse
