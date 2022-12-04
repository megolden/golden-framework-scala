package golden.framework

type Predicate[A] = A => Boolean

type Predicate2[A, B] = (A, B) => Boolean
