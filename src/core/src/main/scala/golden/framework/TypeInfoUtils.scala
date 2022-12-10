package golden.framework

object TypeInfoUtils:
  extension (tpe: TypeInfo)
    def isOptionType: Boolean =
      tpe.symbolName == typeOf[scala.Option[?]].symbolName

inline def typeOf[T]: TypeInfo =
  Macros.getType[T]
