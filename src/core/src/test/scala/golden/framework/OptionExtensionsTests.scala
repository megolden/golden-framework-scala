package golden.framework

import golden.framework.OptionExtensions.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import java.util.Optional

class OptionExtensionsTests extends AnyFunSuite with Matchers:

  test("toOption should be convert java Optional to scala Option") {
    Optional.ofNullable(null).toOption shouldBe None
    Optional.of(1).toOption shouldBe Some(1)
    Optional.empty[Int].toOption shouldBe Option.empty[Int]
  }

  test("toOptional should be convert scala Option to java Optional") {
    None.toOptional shouldBe Optional.empty
    Some(1).toOptional shouldBe Optional.of(1)
    Option.empty[Int].toOptional shouldBe Optional.empty[Int]
  }

  test("tap should be apply function on non empty Option") {
    var emptyCalls = 0
    var someCalls = 0

    Option.empty[Int].tap(v => emptyCalls += v)
    Some(10).tap(v => someCalls += v)

    emptyCalls shouldBe 0
    someCalls shouldBe 10
  }
