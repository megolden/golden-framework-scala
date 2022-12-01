package golden.framework.application

trait CommandHandler[TCommand <: Command[TResponse], TResponse]:
  def handle(command: TCommand): TResponse
