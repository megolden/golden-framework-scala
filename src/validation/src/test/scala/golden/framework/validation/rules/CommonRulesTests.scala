package golden.framework.validation.rules

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.validation.{validatorFor, ValidationException}
import golden.framework.validation.rules.CommonValidators.*

class CommonRulesTests extends AnyFunSuite with Matchers:

  test("validator should evaluate 'required' rule") {
    val validator = validatorFor[String] {
      _.mustBe.required
    }

    noException should be thrownBy validator.validate("someValue")
    (the [ValidationException] thrownBy validator.validate(null)).getMessage shouldBe "must not be empty."
    (the [ValidationException] thrownBy validator.validate(None)).getMessage shouldBe "must not be empty."
  }

  test("validator should evaluate negate 'required' rule") {
    val validator = validatorFor[String] {
      _.mustNotBe.required
    }

    (the[ValidationException] thrownBy validator.validate("someValue")).getMessage shouldBe "must be empty."
    noException should be thrownBy validator.validate(null)
    noException should be thrownBy validator.validate(None)
  }

  test("validator should evaluate 'equal' rule") {
    val validator = validatorFor[String] {
      _.mustBe.equal("someValue")
    }

    noException should be thrownBy validator.validate("someValue")
    (the[ValidationException] thrownBy validator.validate("differentValue"))
      .getMessage shouldBe "must be equal to someValue."
  }

  test("validator should evaluate negate 'equal' rule") {
    val validator = validatorFor[String] {
      _.mustNotBe.equal("someValue")
    }

    noException should be thrownBy validator.validate("differentValue")
    (the[ValidationException] thrownBy validator.validate("someValue"))
      .getMessage shouldBe "must not be equal to someValue."
  }

  test("validator should evaluate rule on property of object") {
    val validator = validatorFor[Person] {
      _.the(_.name).mustNotBe.equal(provider = (_:Person).family)
    }

    noException should be thrownBy validator.validate(Person(name = "aName", family = "aFamily"))
    (the [ValidationException] thrownBy validator.validate(Person(name = "aName", family = "aName")))
      .getMessage shouldBe "name must not be equal to aName."
  }

case class Person(name: String = null, family: String = null)
