package golden.framework.domain

abstract class Entity[TId](val id: TId):

  override def equals(other: Any): Boolean = other match {
    case that: Entity[?] if that.getClass == this.getClass => that.id == this.id
    case _ => false
  }

  override def hashCode: Int =
    id.hashCode

  protected final def objectHashCode: Int =
    super.hashCode
