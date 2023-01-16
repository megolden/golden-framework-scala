package golden.framework.hibernate.udt

import org.hibernate.usertype.ParameterizedType
import java.sql.Types.{INTEGER, VARCHAR}
import java.util.Properties
import scala.reflect.Enum
import golden.framework.ClassUtils.{findEnumFromOrdinalMethod, findEnumFromNameMethod}

class EnumType
  extends BasicSingleColumnType[Enum, Int|String](classOf[Enum])
  with ParameterizedType:

  private var _enumClass: Class[Enum] = _
  private var _enumFromOrdinal: Int => Enum = _
  private var _enumValueOf: String => Enum = _
  private var _storeUsingName = false

  override def getValue(value: Int|String): Enum = {
    if value.isInstanceOf[String] then _enumValueOf(value.asInstanceOf[String])
    else _enumFromOrdinal(value.asInstanceOf[Number].intValue)
  }

  override def setValue(value: Enum): Int|String = {
    if _storeUsingName then value.toString
    else value.ordinal
  }

  override def getSqlType: Int = if _storeUsingName then VARCHAR else INTEGER

  override def setParameterValues(parameters: Properties): Unit = {
    _enumClass = Class.forName(parameters.getProperty("enumType")).asInstanceOf[Class[Enum]]
    _enumFromOrdinal = findEnumFromOrdinalMethod(_enumClass)
    _enumValueOf = findEnumFromNameMethod(_enumClass)
    _storeUsingName = parameters.getProperty("storeUsingName", "false").toBoolean
  }
