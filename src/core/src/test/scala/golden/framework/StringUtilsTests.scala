package golden.framework

import golden.framework.StringUtils.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class StringUtilsTests extends AnyFunSuite with Matchers:

  test("nonBlank") {
    null.nonBlank shouldBe false
    "".nonBlank shouldBe false
    " ".nonBlank shouldBe false
    "bob".nonBlank shouldBe true
    " bob ".nonBlank shouldBe true
  }
