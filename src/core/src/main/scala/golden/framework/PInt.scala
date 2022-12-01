package golden.framework

final case class PInt(value: Int) extends Ordered[PInt] with Serializable:

  if value <= 0 then
    throw IllegalArgumentException(s"value must be greater than zero: $value")

  override def equals(other: Any): Boolean = other match
    case that: PInt if this.getClass == that.getClass =>
      this.value == that.value
    case _ => false

  override def hashCode: Int = value.hashCode

  override def toString: String = value.toString

  override def clone: AnyRef = PInt(value)

  override def compare(that: PInt): Int =
    Option(that).map(_.value).map(this.value.compare).getOrElse(1)

object PInt:
  import scala.language.implicitConversions

  implicit def valueOf(int: Int): PInt = PInt(int)

  implicit def toInt(p: PInt): Int = p.value
