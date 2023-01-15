package golden.framework

import java.lang.reflect.Modifier

object TypeUtils:
  extension (tpe: Type)
    def isOptionType: Boolean =
      tpe.symbolName == Type.of[scala.Option[?]].symbolName

    def isAbstract: Boolean =
      Modifier.isAbstract(tpe.getRawType.getModifiers)

    def isEnum: Boolean =
      classOf[scala.reflect.Enum].isAssignableFrom(tpe.getRawType)

inline def typeOf[T]: Type =
  Macros.getType[T]
