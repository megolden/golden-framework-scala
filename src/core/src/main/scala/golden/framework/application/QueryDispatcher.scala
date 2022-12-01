package golden.framework.application

import golden.framework.{TypeInfo, typeOf}

trait QueryDispatcher:

  inline final def dispatch[TQuery <: Query[TResult], TResult](query: TQuery): TResult =
    dispatch[TQuery, TResult](query, typeOf[TQuery], typeOf[TResult])

  protected def dispatch[TQuery <: Query[TResult], TResult](
    query: TQuery,
    queryType: TypeInfo,
    resultType: TypeInfo): TResult
