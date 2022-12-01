package golden.framework.application

trait QueryHandler[TQuery <: Query[TResult], TResult]:
  def handle(query: TQuery): TResult
