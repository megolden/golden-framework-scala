package golden.framework

import golden.framework.*
import golden.framework.StringUtils.Empty
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ExtensionsTests extends AnyFunSuite with Matchers:

  test("nameOf should return name of field or method") {
    nameOf[String](_.length) should be ("length")
    nameOf[String](_.trim.length) should be ("length")
  }

  test("default should return default value of specified type") {
    default[Long] shouldBe 0L
    default[Int] shouldBe 0
    default[Boolean] shouldBe false
    default[String] shouldBe null
  }

  test("nullAs should return null value of specified type") {
    nullAs[String] shouldBe null
  }

  test("emptyString should be empty string value") {
    Empty shouldBe ""
  }

  test("isNull and nonNull should works properly") {
    val nullString: String = null
    val string = ""

    nullString.isNull shouldBe true
    string.isNull shouldBe false
    nullString.nonNull shouldBe false
    string.nonNull shouldBe true
  }

  test("isNullOrNone and nonNullOrNone should works properly") {
    val nullValue: String = null
    val noneValue = None
    val string = ""
    val someValue = Some(1)

    nullValue.isNullOrNone shouldBe true
    noneValue.isNullOrNone shouldBe true
    string.isNullOrNone shouldBe false
    someValue.isNullOrNone shouldBe false
    nullValue.nonNullOrNone shouldBe false
    noneValue.nonNullOrNone shouldBe false
    string.nonNullOrNone shouldBe true
    someValue.nonNullOrNone shouldBe true
  }

  test("isNone and nonNone should works properly") {
    val nullValue: String = null
    val noneValue = None
    val string = ""
    val someValue = Some(1)

    nullValue.isNone shouldBe false
    noneValue.isNone shouldBe true
    string.isNone shouldBe false
    someValue.isNone shouldBe false
    nullValue.nonNone shouldBe true
    noneValue.nonNone shouldBe false
    string.nonNone shouldBe true
    someValue.nonNone shouldBe true
  }

  test("unwrapOption should works properly") {
    val noneValue = Option.empty[String]
    val someValue = Some(1)
    val string = "dummy"

    noneValue.unwrapOption.asInstanceOf shouldBe null
    someValue.unwrapOption shouldBe 1
    string.unwrapOption shouldBe "dummy"
  }
