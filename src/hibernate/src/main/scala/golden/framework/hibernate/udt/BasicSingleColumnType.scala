package golden.framework.hibernate.udt

import golden.framework.CommonExtensions.isNullOrNone
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.UserType
import java.sql.{PreparedStatement, ResultSet}
import java.util.Objects
import java.sql.Types.OTHER

abstract class BasicSingleColumnType[TModel, TValue](
  modelClass: Class[TModel],
  sqlType: Int,
  setter: TModel => TValue,
  getter: TValue => TModel)
  extends UserType[TModel]:

  private val _isOption: Boolean = classOf[Option[?]].isAssignableFrom(modelClass)

  def this(modelClass: Class[TModel]) =
    this(modelClass, OTHER, _.asInstanceOf[TValue], _.asInstanceOf[TModel])

  final override def nullSafeGet(
    resultSet: ResultSet,
    position: Int,
    session: SharedSessionContractImplementor,
    owner: Any): TModel = {

    val value = resultSet.getObject(position)
    if (resultSet.wasNull)
      (if _isOption then scala.None else null).asInstanceOf[TModel]
    else
      getValue(value.asInstanceOf[TValue])
  }

  final override def nullSafeSet(
    statement: PreparedStatement,
    value: TModel,
    index: Int,
    session: SharedSessionContractImplementor): Unit = {

    if value.isNullOrNone then statement.setNull(index, sqlType)
    else statement.setObject(index, setValue(value), sqlType)
  }

  def getValue(value: TValue): TModel = getter.apply(value)

  def setValue(value: TModel): TValue = setter.apply(value)

  override def getSqlType: Int = sqlType

  final override def returnedClass(): Class[TModel] = modelClass

  final override def equals(x: TModel, y: TModel): Boolean = Objects.equals(x, y)

  final override def hashCode(model: TModel): Int = Objects.hashCode(model)

  final override def deepCopy(value: TModel): TModel = value

  final override def isMutable: Boolean = false

  final override def disassemble(value: TModel): Serializable = value.asInstanceOf[Serializable]

  final override def assemble(cached: Serializable, owner: Any): TModel = cached.asInstanceOf[TModel]

  final override def replace(detached: TModel, managed: TModel, owner: Any): TModel = detached
