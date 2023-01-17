package golden.framework

private class WildcardTypeImpl(val low: Type, val hi: Type) extends TypeImpl("?") with WildcardType
