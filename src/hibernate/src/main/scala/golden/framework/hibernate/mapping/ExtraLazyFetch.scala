package golden.framework.hibernate.mapping

enum ExtraLazyFetch(name: String):
  case True extends ExtraLazyFetch("true")
  case False extends ExtraLazyFetch("false")
  case Extra extends ExtraLazyFetch("extra")

  override def toString: String = name
