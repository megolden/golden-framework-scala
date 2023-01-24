package golden.framework

trait IntersectionType extends Type:
  val left: Type
  val right: Type

  override def name: String = s"${left.name} & ${right.name}"
