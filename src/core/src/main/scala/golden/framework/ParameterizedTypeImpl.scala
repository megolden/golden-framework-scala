package golden.framework

private class ParameterizedTypeImpl(symbolName: String, val args: Seq[Type])
  extends TypeImpl(symbolName)
    with ParameterizedType
