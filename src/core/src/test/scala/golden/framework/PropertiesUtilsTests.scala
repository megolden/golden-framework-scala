package golden.framework

import golden.framework.PropertiesUtils.*
import org.scalatest.Entry
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.util.Properties

class PropertiesUtilsTests extends AnyFunSuite with Matchers:

  test("withPrefix should return properties with specified prefix") {
    val props = new Properties {
      put("a", 1)
      put("a.b", 11)
      put("a.c", 12)
      put("a.c.cc", 122)
      put("d", 2)
      put("d.e", 21)
      put("d.f", 22)
    }

    val actualProps = props.withPrefix("a")

    actualProps should contain only (Entry("b", 11), Entry("c", 12), Entry("c.cc", 122))
  }
