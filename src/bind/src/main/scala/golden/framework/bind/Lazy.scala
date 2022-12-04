package golden.framework.bind

trait Lazy[T]:
  lazy val get: T
