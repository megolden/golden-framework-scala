package golden.framework.domain

import java.util.Objects.hash

abstract class ValueObject:

  protected def uniquenessValues: Seq[Any]

  override def equals(other: Any): Boolean = other match {
    case that: ValueObject if that.getClass == this.getClass =>
      that.uniquenessValues == this.uniquenessValues
    case _ => false
  }

  override def hashCode: Int =
    hash(uniquenessValues*)
