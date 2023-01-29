package golden.framework.hibernate.mapping

enum ExtraLazyFetch(val value: String):
  case True extends ExtraLazyFetch("true")
  case False extends ExtraLazyFetch("false")
  case Extra extends ExtraLazyFetch("extra")
