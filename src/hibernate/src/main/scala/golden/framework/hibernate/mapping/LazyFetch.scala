package golden.framework.hibernate.mapping

enum LazyFetch(name: String):
  case False extends LazyFetch("false")
  case Proxy extends LazyFetch("proxy")

  override def toString: String = name
