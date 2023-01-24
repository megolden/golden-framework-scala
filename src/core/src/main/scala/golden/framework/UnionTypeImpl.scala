package golden.framework

private class UnionTypeImpl(val left: Type, val right: Type)
  extends TypeImpl("?", classOf[Object])
    with UnionType
