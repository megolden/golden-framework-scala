package golden.framework.validation

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.StringUtils.nonBlank
import java.time.LocalDate

class ValidatorTests extends AnyFunSuite with Matchers:

  test("validator should evaluate value with specified rule") {
    val validator = validatorFor[String] {
      _.must(nonBlank)
    }

    a [ValidationException] should be thrownBy validator.validate(null)
    a [ValidationException] should be thrownBy validator.validate("")
    a [ValidationException] should be thrownBy validator.validate(" ")
    noException should be thrownBy validator.validate(" a ")
  }

  test("validator should throw exception with specified message and format") {
    val validator = validatorFor[String] {
      _.must(_.equals("mehdi")).withMessage("invalid name: %s")
    }

    val caught = the [ValidationException] thrownBy validator.validate("reza")

    caught.getMessage shouldBe "invalid name: reza"
  }

  test("validator should throw specified failure exception") {
    val validator = validatorFor[String] {
      _.must(nonBlank).withFailure(_ => SomeException())
    }

    a [SomeException] should be thrownBy validator.validate(null)
  }

  test("validator should evaluate rule with precondition") {
    val validator = validatorFor[Person] {
      _.must(_.isMarried == true).when(_.children > 0)
    }

    noException should be thrownBy validator.validate(Person(children = 1, isMarried = true))
    noException should be thrownBy validator.validate(Person(children = 0, isMarried = true))
    noException should be thrownBy validator.validate(Person(children = 0, isMarried = false))
    a [ValidationException] should be thrownBy validator.validate(Person(children = 1, isMarried = false))
  }

  test("validator should evaluate property of object") {
    val validator = validatorFor[String] {
      _.the(_.length).must(_ == 6)
    }

    noException should be thrownBy validator.validate("123456")
    an [ValidationException] should be thrownBy validator.validate("1234")
    an [ValidationException] should be thrownBy validator.validate("1234567")
  }

  test("validator should evaluate property of object depend on another property") {
    val validator = validatorFor[Person] {
      _.the(_.childBirthDate).must((childBirthDate, father) => childBirthDate.isAfter(father.birthDate))
    }

    noException should be thrownBy
      validator.validate(Person(birthDate = LocalDate.of(2000, 1, 1), childBirthDate = LocalDate.of(2020, 1, 1)))
    a [ValidationException] should be thrownBy
      validator.validate(Person(birthDate = LocalDate.of(2000, 1, 1), childBirthDate = LocalDate.of(1999, 1, 1)))
  }

  test("validator should evaluate object by custom validator") {
    val validator = new MarriedPersonValidator

    noException should be thrownBy validator.validate(Person(isMarried = true))
    a [ValidationException] should be thrownBy validator.validate(Person(isMarried = false))
  }

  test("validator should evaluate property by custom validator") {
    val aYoungChild = validatorFor[Person](_.the(_.age).must(_ <= 20))
    val validator = validatorFor[Person] {
      _.the(_.child).hasValidator(aYoungChild)
    }

    noException should be thrownBy validator.validate(Person(child = Person(age = 15)))
    a [ValidationException] should be thrownBy validator.validate(Person(child = Person(age = 40)))
  }

  test("validator should evaluate option property when has value") {
    val validator = validatorFor[Person] {
      _.theOption(_.deathDate).must((deathDate, person) => deathDate.isAfter(person.birthDate))
    }

    noException should be thrownBy validator.validate(Person(birthDate = LocalDate.of(2000, 1, 1), deathDate = None))
    noException should be thrownBy
      validator.validate(Person(birthDate = LocalDate.of(2000, 1, 1), deathDate = Some(LocalDate.of(2100, 1, 1))))
    a [ValidationException] should be thrownBy
      validator.validate(Person(birthDate = LocalDate.of(2000, 1, 1), deathDate = Some(LocalDate.of(1990, 1, 1))))
  }

class SomeException extends Exception

case class Person(
  name: String = null,
  isMarried: Boolean = false,
  children: Int = 0,
  birthDate: LocalDate = null,
  childBirthDate: LocalDate = null,
  age: Int = 0,
  child: Person = null,
  deathDate: Option[LocalDate] = None)

class MarriedPersonValidator extends ObjectValidator[Person]:
  the(_.isMarried).must(_ == true)
