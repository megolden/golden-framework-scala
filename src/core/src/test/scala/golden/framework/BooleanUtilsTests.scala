package golden.framework

import golden.framework.BooleanUtils.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BooleanUtilsTests extends AnyFunSuite with Matchers:

  test("not") {
    BooleanUtils.not(true) shouldBe false
    BooleanUtils.not(false) shouldBe true
  }

  test("and") {
    true and true shouldBe true
    true and false shouldBe false
    false and true shouldBe false
    false and false shouldBe false
  }

  test("or") {
    true or true shouldBe true
    true or false shouldBe true
    false or true shouldBe true
    false or false shouldBe false
  }
