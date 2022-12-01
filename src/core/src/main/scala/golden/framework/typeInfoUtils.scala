package golden.framework

extension (tpe: TypeInfo)

  def isOption: Boolean =
    tpe.symbolName == typeOf[scala.Option[?]].symbolName

end extension

inline def typeOf[T]: TypeInfo =
  Macros.getTypeInfo[T]
