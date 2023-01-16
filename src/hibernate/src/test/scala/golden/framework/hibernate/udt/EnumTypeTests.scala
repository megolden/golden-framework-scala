package golden.framework.hibernate.udt

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import java.sql.Types.{VARCHAR, INTEGER}
import java.util.Properties

class EnumTypeTests extends AnyFunSuite with Matchers:

  test("enum should store using ordinal value properly") {
    val enumType = new EnumType {
      setParameterValues(new Properties {
        setProperty("enumType", classOf[SomeEnum].getName)
        setProperty("storeUsingName", "false")
      })
    }

    enumType.getSqlType shouldBe INTEGER
  }

  test("enum should store using name properly") {
    val enumType = new EnumType {
      setParameterValues(new Properties {
        setProperty("enumType", classOf[SomeEnum].getName)
        setProperty("storeUsingName", "true")
      })
    }

    enumType.getSqlType shouldBe VARCHAR
  }

  test("option enum should store using ordinal value properly") {
    val enumType = new OptionEnumType {
      setParameterValues(new Properties {
        setProperty("enumType", classOf[SomeEnum].getName)
        setProperty("storeUsingName", "false")
      })
    }

    enumType.getSqlType shouldBe INTEGER
  }

  test("option enum should store using name properly") {
    val enumType = new OptionEnumType {
      setParameterValues(new Properties {
        setProperty("enumType", classOf[SomeEnum].getName)
        setProperty("storeUsingName", "true")
      })
    }

    enumType.getSqlType shouldBe VARCHAR
  }

enum SomeEnum { case A, B, C }
