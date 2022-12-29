package golden.framework.hibernate.mapping

enum NoProxyLazyFetch(name: String):
  case False extends NoProxyLazyFetch("false")
  case Proxy extends NoProxyLazyFetch("proxy")
  case NoProxy extends NoProxyLazyFetch("no-proxy")

  override def toString: String = name
