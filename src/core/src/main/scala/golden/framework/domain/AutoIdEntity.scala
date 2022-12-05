package golden.framework.domain

import golden.framework.default

abstract class AutoIdEntity[TId] extends Entity[TId](default[TId]):

  private def hasId: Boolean = id != default[TId]

  override def equals(other: Any): Boolean = {
    (other.isInstanceOf[AutoIdEntity[?]] && this.eq(other.asInstanceOf[AutoIdEntity[?]])) ||
    (hasId && super.equals(other))
  }

  override def hashCode: Int = {
    if hasId then super.hashCode
    else objectHashCode
  }

object AutoIdEntity:

  def setId[TId](entity: AutoIdEntity[TId], id: TId): Unit = {
    val idField = classOf[Entity[?]].getDeclaredField("id")
    idField.setAccessible(true)
    idField.set(entity, id)
  }
