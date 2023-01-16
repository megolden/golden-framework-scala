package golden.framework.hibernate.udt

import org.hibernate.usertype.ParameterizedType
import java.sql.Types.{INTEGER, VARCHAR}
import java.util.Properties
import scala.reflect.Enum
import golden.framework.ClassUtils.{findEnumFromOrdinalMethod, findEnumFromNameMethod}

class OptionEnumType
  extends BasicSingleColumnType[Option[Enum], Int|String](classOf[Option[Enum]])
  with ParameterizedType:

  private var _enumClass: Class[Enum] = _
  private var _enumFromOrdinal: Int => Enum = _
  private var _enumValueOf: String => Enum = _
  private var _storeUsingName = false

  override def getValue(value: Int|String): Option[Enum] = {
    if value.isInstanceOf[String] then Some(_enumValueOf(value.asInstanceOf[String]))
    else Some(_enumFromOrdinal(value.asInstanceOf[Number].intValue))
  }

  override def setValue(value: Option[Enum]): Int|String = {
    if _storeUsingName then value.get.toString
    else value.get.ordinal
  }

  override def getSqlType: Int = if _storeUsingName then VARCHAR else INTEGER

  override def setParameterValues(parameters: Properties): Unit = {
    _enumClass = Class.forName(parameters.getProperty("enumType")).asInstanceOf[Class[Enum]]
    _enumFromOrdinal = findEnumFromOrdinalMethod(_enumClass)
    _enumValueOf = findEnumFromNameMethod(_enumClass)
    _storeUsingName = parameters.getProperty("storeUsingName", "false").toBoolean
  }
