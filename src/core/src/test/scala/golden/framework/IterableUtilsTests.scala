package golden.framework

import golden.framework.IterableUtils.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class IterableUtilsTests extends AnyFunSuite with Matchers:

  test("ifEmpty") {
    an [Exception] should be thrownBy {
      Nil.ifEmpty { () => throw Exception("is empty") }
    }

    noException should be thrownBy {
      List(1,2,3).ifEmpty { () => throw Exception("is not empty") }
    }
  }
