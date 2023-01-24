package golden.framework

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.CommonExtensions.*
import golden.framework.StringUtils.Empty

class CommonExtensionsTests extends AnyFunSuite with Matchers:

  test("nameOf should return name of field or method") {
    inline def getNameOfLambda[T](inline p: T => ?): String = nameOf(p)

    getNameOfLambda[String](str => str) should be ("str")
    getNameOfLambda[String](_.length) should be ("length")
    getNameOfLambda[String](_.trim.length) should be ("length")
    getNameOfLambda[String](_.trim.length.toString) should be ("toString")
    getNameOfLambda[String](_.trim.length.toString.lengthIs) should be ("lengthIs")
    getNameOfLambda[String](_.trim.length.toString.lengthIs.toLong) should be ("toLong")
  }

  test("fullNameOf should return full name of call chain of call") {
    val firstName: String = "some"
    def getParamName(param: String): String = fullNameOf(param)
    object Obj { def func: String = "" }
    inline def getFullNameOfLambda[T](inline p: T => ?): String = fullNameOf(p)

    fullNameOf(firstName) should be ("firstName")
    fullNameOf(firstName) should be ("firstName")
    fullNameOf(firstName.length) should be ("firstName.length")
    fullNameOf(firstName.length.toString) should be ("firstName.length.toString")
    getParamName(firstName) should be ("param")
    fullNameOf(Obj.func) should be ("Obj.func")
    fullNameOf((_:String).length) should be ("length")
    getFullNameOfLambda[String](_.length) should be ("length")
    getFullNameOfLambda[String](_.trim.length) should be ("trim.length")
    getFullNameOfLambda[String](_.trim.length.toString) should be ("trim.length.toString")
    getFullNameOfLambda[String](_.trim.length.toString.lengthIs) should be ("trim.length.toString.lengthIs")
    getFullNameOfLambda[String](_.lengthIs) should be ("lengthIs")
    getFullNameOfLambda[String](_.trim.length.toString.lengthIs.toLong) should be ("trim.length.toString.lengthIs.toLong")
  }

  test("default should return default value of specified type") {
    default[Byte] shouldBe (0:Byte)
    default[Short] shouldBe (0:Short)
    default[Int] shouldBe 0
    default[Long] shouldBe 0L
    default[Float] shouldBe 0F
    default[Double] shouldBe 0D
    default[Boolean] shouldBe false
    default[Char] shouldBe '\u0000'
    default[Unit] shouldBe ()
    default[String] shouldBe null
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

  test("isOption should return true when an Option value passed") {
    val noneValue = None
    val someValue = Some(0)
    val string = "something"

    noneValue.isOption shouldBe true
    someValue.isOption shouldBe true
    string.isOption shouldBe false
  }
