package golden.framework

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.StringUtils.{isEmpty as IsEmpty, isBlank as IsBlank, nonBlank as NonBlank, nonEmpty as NonEmpty}

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

  test("nonBlank should return true when string is not null nor blank") {
    null.NonBlank shouldBe false
    "".NonBlank shouldBe false
    " ".NonBlank shouldBe false
    "bob".NonBlank shouldBe true
    " bob ".NonBlank shouldBe true
  }

  test("nonEmpty should return true when string is not null nor empty") {
    null.NonEmpty shouldBe false
    "".NonEmpty shouldBe false
    " ".NonEmpty shouldBe true
    "bob".NonEmpty shouldBe true
    " bob ".NonEmpty shouldBe true
  }
