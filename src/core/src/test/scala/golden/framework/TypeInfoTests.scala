package golden.framework

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TypeInfoTests extends AnyFunSuite with Matchers:

  test("fromClass should return TypeInfo from specified class") {
    val clsType = TypeInfo.fromClass(classOf[String])

    clsType should be (typeOf[String])
  }

  test("fromClass should ignore class type arguments") {
    val clsType = TypeInfo.fromClass(classOf[List[String]])

    clsType should be (typeOf[List[?]])
  }

  test("equals and hashCode should be based on name") {
    val t1 = typeOf[List[Int]]
    val t2 = typeOf[List[Int]]

    t1.equals(t2) shouldBe true
    t1.hashCode shouldBe t2.hashCode
  }

  test("withArgs should return new type with specified arguments") {
    val tpe = typeOf[List[Int]].withArgs(typeOf[Long])

    tpe.args should contain only typeOf[Long]
  }

  test("asClass should return equivalent class") {
    val cls = typeOf[List[Int]].asClass

    cls should be (classOf[List[Int]])
  }

  test("isOption should return true when option type passed") {
    val optionType = typeOf[Option[?]]
    val nonOptionType = typeOf[String]

    optionType.isOption shouldBe true
    nonOptionType.isOption shouldBe false
  }
