package golden.framework

private class IntersectionTypeImpl(val left: Type, val right: Type)
  extends TypeImpl(left.symbolName + " & " + right.symbolName)
    with IntersectionType:

  override protected def internalGetRawType: Class[?] = classOf[Object]

  override def name: String = left.name + " & " + right.name
