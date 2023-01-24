package golden.framework

private class IntersectionTypeImpl(val left: Type, val right: Type)
  extends TypeImpl("?", classOf[Object])
    with IntersectionType
