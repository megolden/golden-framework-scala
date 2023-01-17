package golden.framework

private class UnionTypeImpl(val left: Type, val right: Type)
  extends TypeImpl(left.symbolName + " | " + right.symbolName)
    with UnionType:

  override protected def internalGetRawType: Class[?] = classOf[Object]

  override def name: String = left.name + " | " + right.name
