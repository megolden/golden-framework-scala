package golden.framework.validation.annotations

import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.AnyFunSuite
import golden.framework.validation.ValidationException
import golden.framework.validation.Validator.validate
import java.time.LocalDateTime

class AnnotationsTests extends AnyFunSuite with Matchers:

  test("validator should evaluate annotated object") {
    a [ValidationException] should be thrownBy validate(SomeAnnotated(name = null))
    a [ValidationException] should be thrownBy validate(SomeAnnotated(name = ""))
    a [ValidationException] should be thrownBy validate(SomeAnnotated(name = " "))
    noException should be thrownBy validate(SomeAnnotated(name = " someValue "))
  }

  test("validator should evaluate equal annotation properly") {
    a [ValidationException] should be thrownBy validate(SomeAnnotated(name = "a", nameRepeat = "b"))
    a [ValidationException] should be thrownBy validate(SomeAnotherAnnotated(oneHundred = 50))
    noException should be thrownBy validate(SomeAnnotated(name = "a", nameRepeat = "a"))
    noException should be thrownBy validate(SomeAnotherAnnotated(oneHundred = 100))
  }

  test("validator should evaluate future annotation properly") {
    a [ValidationException] should be thrownBy validate(SomeTimeAnnotated(futureTime = LocalDateTime.now))
    noException should be thrownBy validate(SomeTimeAnnotated(futureTime = LocalDateTime.now.plusDays(1)))
  }

  test("validator should evaluate max annotation properly") {
    a [ValidationException] should be thrownBy validate(SomeMaxMinAnnotated(max100 = 101))
    noException should be thrownBy validate(SomeMaxMinAnnotated(max100 = 100))
  }

case class SomeAnnotated(
  @required @notBlank name: String = null,

  @equal(_.asInstanceOf[SomeAnnotated].name)
  nameRepeat: String = null
)

case class SomeAnotherAnnotated(@equal(100) oneHundred: Int)

case class SomeTimeAnnotated(@future futureTime: LocalDateTime)

case class SomeMaxMinAnnotated(@max(BigInt(100)) max100: BigInt)
