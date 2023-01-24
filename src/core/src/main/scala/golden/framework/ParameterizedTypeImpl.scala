package golden.framework

private class ParameterizedTypeImpl(symbolName: String, rawType: Class[?], val args: Seq[Type])
  extends TypeImpl(symbolName, rawType)
    with ParameterizedType
