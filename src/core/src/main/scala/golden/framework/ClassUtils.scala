package golden.framework

import scala.reflect.Enum

object ClassUtils:

  def findEnumFromOrdinalMethod[T <: Enum](enumType: Class[T]): Int => T = {
    val fromOrdinal = enumType.getDeclaredMethod("fromOrdinal", classOf[Int])
    ordinal => fromOrdinal.invoke(null, ordinal).asInstanceOf[T]
  }

  def findEnumFromNameMethod[T <: Enum](enumType: Class[T]): String => T = {
    val valueOf = enumType.getDeclaredMethod("valueOf", classOf[String])
    name => valueOf.invoke(null, name).asInstanceOf[T]
  }
