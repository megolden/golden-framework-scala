package golden.framework.validation

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GuardTests extends AnyFunSuite with Matchers:

  test("rule guard should work properly") {
    noException should be thrownBy Guard.against(true)
    an [IllegalArgumentException] should be thrownBy Guard.against(false)
  }

  test("nullOrNone guard should work properly") {
    noException should be thrownBy Guard.Against.nullOrNone("some")
    noException should be thrownBy Guard.Against.nullOrNone(0)
    an [IllegalArgumentException] should be thrownBy Guard.Against.nullOrNone(null)
    an [IllegalArgumentException] should be thrownBy Guard.Against.nullOrNone(None)
  }

  test("empty guard should work properly") {
    noException should be thrownBy Guard.Against.empty("some")
    noException should be thrownBy Guard.Against.empty(List(1))
    an [IllegalArgumentException] should be thrownBy Guard.Against.empty("")
    an [IllegalArgumentException] should be thrownBy Guard.Against.empty(List.empty)
  }

  test("blank guard should work properly") {
    noException should be thrownBy Guard.Against.blank("some")
    an [IllegalArgumentException] should be thrownBy Guard.Against.blank(null)
    an [IllegalArgumentException] should be thrownBy Guard.Against.blank("")
    an [IllegalArgumentException] should be thrownBy Guard.Against.blank(" ")
  }

  test("zero guard should work properly") {

    noException should be thrownBy Guard.Against.zero(1)
    noException should be thrownBy Guard.Against.zero(-1)

    the [IllegalArgumentException] thrownBy {
      Guard.Against.zero(0)
    } should have message "argument cannot be zero"

    the [IllegalArgumentException] thrownBy {
      Guard.Against.zero(0, name = "Code")
    } should have message "Code cannot be zero"

    the [IllegalArgumentException] thrownBy {
      Guard.Against.zero(0, message = "'Code' HAS INVALID VALUE.")
    } should have message "'Code' HAS INVALID VALUE."

    a [SomeExceptionClass] should be thrownBy {
      Guard.Against.zero(0, exception = SomeExceptionClass())
    }
  }

  test("negativeOrZero guard should work properly") {
    noException should be thrownBy Guard.Against.zero(1)
    an [IllegalArgumentException] should be thrownBy Guard.Against.negativeOrZero(0)
    an [IllegalArgumentException] should be thrownBy Guard.Against.negativeOrZero(-1)
  }

  test("negative guard should work properly") {
    noException should be thrownBy Guard.Against.negative(0)
    noException should be thrownBy Guard.Against.negative(1)
    an [IllegalArgumentException] should be thrownBy Guard.Against.negativeOrZero(-1)
  }

  test("null guard should work properly") {
    noException should be thrownBy Guard.Against.beNull(0)
    noException should be thrownBy Guard.Against.beNull(None)
    an [IllegalArgumentException] should be thrownBy Guard.Against.beNull(null)
  }

  test("equal guard should work properly") {
    noException should be thrownBy Guard.Against.equal(0, 0)
    noException should be thrownBy Guard.Against.equal(null, null)
    an [IllegalArgumentException] should be thrownBy Guard.Against.equal(0, 1)
  }

class SomeExceptionClass(message: String = "") extends IllegalArgumentException(message)
