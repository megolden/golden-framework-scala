package golden.framework

import golden.framework.BooleanUtils.not

type Predicate[A] = A => Boolean

type Predicate2[A, B] = (A, B) => Boolean

class MemberDescriptor(val name: String, val tpe: TypeInfo, val isField: Boolean, val annotations: Iterable[?] = Nil):

  val isMethod: Boolean = not(isField)

  override def toString: String = {
    s"$name${if isMethod then "(...)" else ""}: $tpe"
  }

  override def equals(other: Any): Boolean = other match {
    case that: MemberDescriptor => this.name == that.name
    case _ => false
  }

  override def hashCode: Int = {
    name.hashCode
  }
