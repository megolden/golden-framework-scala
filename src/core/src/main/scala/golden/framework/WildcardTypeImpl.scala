package golden.framework

private class WildcardTypeImpl(val low: Type, val hi: Type)
  extends TypeImpl("?", classOf[Object])
    with WildcardType:

  def this(low: Option[Type], hi: Option[Type]) =
    this(low.getOrElse(Type.of[Nothing]), hi.getOrElse(Type.of[Any]))
