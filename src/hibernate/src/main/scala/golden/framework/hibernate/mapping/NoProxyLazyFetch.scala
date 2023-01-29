package golden.framework.hibernate.mapping

enum NoProxyLazyFetch(val value: String):
  case False extends NoProxyLazyFetch("false")
  case Proxy extends NoProxyLazyFetch("proxy")
  case NoProxy extends NoProxyLazyFetch("no-proxy")
