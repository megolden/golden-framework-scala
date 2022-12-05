package golden.framework

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.StringUtils.{isEmpty as IsEmpty, isBlank as IsBlank}

class StringUtilsTests extends AnyFunSuite with Matchers:

  test("isBlank should return true when string is null or blank") {
    null.IsBlank shouldBe true
    "".IsBlank shouldBe true
    " ".IsBlank shouldBe true
    "bob".IsBlank shouldBe false
    " bob ".IsBlank shouldBe false
  }

  test("isEmpty should return true when string is null or empty") {
    null.IsEmpty shouldBe true
    "".IsEmpty shouldBe true
    " ".IsEmpty shouldBe false
    "bob".IsEmpty shouldBe false
    " bob ".IsEmpty shouldBe false
  }
