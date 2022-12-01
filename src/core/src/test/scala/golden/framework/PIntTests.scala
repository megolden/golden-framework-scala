package golden.framework

import golden.framework.PInt.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PIntTests extends AnyFunSuite with Matchers:

  test("PInt should be convert to Int implicitly") {
    val int: Int = PInt(100)

    int shouldBe 100
  }

  test("Int should be convert to PInt implicitly") {
    val pi: PInt = 100

    pi.value shouldBe 100
  }

  test("Constructor should be construct PInt properly") {
    val pi = new PInt(100)

    pi.value shouldBe 100
  }

  test("Constructor should throw exception when passed value is less than or equal to zero") {
    an [IllegalArgumentException] should be thrownBy new PInt(0)
    an [IllegalArgumentException] should be thrownBy new PInt(-1)
  }

  test("PInt equality should be by value") {
    val pi1 = new PInt(1)
    val pi2 = new PInt(1)

    pi1 should equal (pi2)
    pi1.hashCode shouldBe pi2.hashCode
  }

  test("PInt comparison should work properly") {
    val pi1 = new PInt(1)
    val pi2 = new PInt(3)

    (pi1 < pi2) shouldBe true
    (pi1 > pi2) shouldBe false
    (pi1 <= pi2) shouldBe true
    (pi1 >= pi2) shouldBe false
    pi1.compare(pi2) shouldBe -1
  }

  test("PInt clone should work properly") {
    val pi = new PInt(1)
    val piClone = pi.clone.asInstanceOf[PInt]

    pi should equal (piClone)
    piClone shouldNot be theSameInstanceAs pi
  }
