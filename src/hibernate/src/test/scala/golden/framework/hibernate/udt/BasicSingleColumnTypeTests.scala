package golden.framework.hibernate.udt

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.any
import java.sql.Types.VARCHAR
import java.sql.{PreparedStatement, ResultSet}
import org.hibernate.engine.spi.SharedSessionContractImplementor

class BasicSingleColumnTypeTests extends AnyFunSuite with Matchers:

  test("null value should handle implicitly") {
    val nullableType = spy(new SomeNullableUserType)
    val resultSet = mock(classOf[ResultSet])
    when(resultSet.wasNull()).thenReturn(true)

    nullableType.nullSafeSet(mock(classOf[PreparedStatement]), null, 0, mock(classOf[SharedSessionContractImplementor]))
    val result = nullableType.nullSafeGet(resultSet, 0, mock(classOf[SharedSessionContractImplementor]), null)

    result shouldBe null
    verify(nullableType, never()).setValue(any())
    verify(nullableType, never()).getValue(any())
  }

  test("None value should handle implicitly") {
    val optionType = spy(new SomeOptionUserType)
    val resultSet = mock(classOf[ResultSet])
    when(resultSet.wasNull).thenReturn(true)

    optionType.nullSafeSet(mock(classOf[PreparedStatement]), None, 0, mock(classOf[SharedSessionContractImplementor]))
    val result = optionType.nullSafeGet(resultSet, 0, mock(classOf[SharedSessionContractImplementor]), null)

    result shouldBe None
    verify(optionType, never()).setValue(any())
    verify(optionType, never()).getValue(any())
  }

class SomeNullableUserType extends BasicSingleColumnType[String, String](
  classOf[String],
  VARCHAR,
  model => model.toString,
  value => value.toString)

class SomeOptionUserType extends BasicSingleColumnType[Option[String], String](
  classOf[Option[String]],
  VARCHAR,
  model => model.get,
  value => Some(value))
