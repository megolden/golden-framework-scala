package golden.framework.hibernate.mapping

enum LazyFetch(val value: String):
  case False extends LazyFetch("false")
  case Proxy extends LazyFetch("proxy")
