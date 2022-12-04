package golden.framework.time

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import java.time.format.DateTimeParseException
import java.time.DayOfWeek.*
import PersianMonth.*
import java.time.Month.MAY
import org.scalatest.prop.TableDrivenPropertyChecks
import java.time.{DateTimeException, Instant, LocalDate, ZoneId, ZoneOffset}

class PersianDateTests extends AnyFunSuite with Matchers with TableDrivenPropertyChecks:

  test("of should construct a persian date properly") {
    val date = PersianDate.of(1399, 12, 30)

    date.year shouldBe 1399
    date.month shouldBe Esfand
    date.dayOfMonth shouldBe 30
    date.dayOfWeek shouldBe SATURDAY
    date.dayOfYear shouldBe 366
    date.isLeapYear shouldBe true
  }

  test("of should construct a persian date properly with month argument") {
    val date = PersianDate.of(1399, Esfand, 30)

    date.year shouldBe 1399
    date.month shouldBe Esfand
    date.dayOfMonth shouldBe 30
  }

  test("persian date text should parse properly") {
    val date = PersianDate.parse("1399/12/9")

    date.year shouldBe 1399
    date.month shouldBe Esfand
    date.dayOfMonth shouldBe 9
    date.dayOfWeek shouldBe SATURDAY
    date.dayOfYear shouldBe 345
    date.isLeapYear shouldBe true
  }

  test("parse should throw exception when invalid date text passed") {
    val invalidDateFormats = Table("dateText",
      "/1/1",             // without year
      "1234567/1/1",      // with exceeded year digits
      "1//1",             // without month
      "1/123/1",          // with exceeded month digits
      "1/1/"              // without day
    )
    forAll(invalidDateFormats) { (dateText: String) =>
      a[DateTimeParseException] should be thrownBy {
        PersianDate.parse(dateText)
      }
    }
  }

  test("of should throw exception when invalid date value passed") {
    val invalidDates = Table(("year", "month", "day"),
      (-1000_000, 1, 1),  // with exceeded minimum year
      (1000_000, 1, 1),   // with exceeded maximum year
      (0, 1, 1),          // with zero year
      (1, 0, 1),          // with exceeded minimum month
      (1, 13, 1),         // with exceeded maximum month
      (1, 1, 0),          // with exceeded minimum day
      (1, 1, 32)          // with exceeded maximum day
    )
    forAll(invalidDates) { (year: Int, month: Int, day: Int) =>
      a[DateTimeException] should be thrownBy {
        PersianDate.of(year, month, day)
      }
    }
  }

  test("persian date should be convert from a time instant") {
    val date = PersianDate.ofInstant(Instant.parse("2022-05-25T19:30:00.000000Z"))

    date.year shouldBe 1401
    date.month shouldBe Khordad
    date.dayOfMonth shouldBe 5
    date.dayOfWeek shouldBe THURSDAY
    date.dayOfYear shouldBe 67
    date.isLeapYear shouldBe false
  }

  test("persian date should be convert from a local date") {
    val date = PersianDate.from(LocalDate.parse("2022-05-25"), ZoneOffset.UTC)

    date.year shouldBe 1401
    date.month shouldBe Khordad
    date.dayOfMonth shouldBe 4
    date.dayOfWeek shouldBe WEDNESDAY
    date.dayOfYear shouldBe 66
    date.isLeapYear shouldBe false
  }

  test("persian date should be convert from number of days of year") {
    val date = PersianDate.ofYearDay(1400, 187)

    date.year shouldBe 1400
    date.month shouldBe Mehr
    date.dayOfMonth shouldBe 1
    date.dayOfWeek shouldBe THURSDAY
    date.dayOfYear shouldBe 187
    date.isLeapYear shouldBe false
  }

  test("daysInMonth should return number of days in a month") {
    val dayOfMonths = Table(("month", "year", "days"),
      (1, 1400, 31),
      (2, 1400, 31),
      (3, 1400, 31),
      (4, 1400, 31),
      (5, 1400, 31),
      (6, 1400, 31),
      (7, 1400, 30),
      (8, 1400, 30),
      (9, 1400, 30),
      (10, 1400, 30),
      (11, 1400, 30),
      (12, 1400, 29),
      (12, 1399, 30) // leap year
    )
    forAll(dayOfMonths) { (month: Int, year: Int, days: Int) =>
      PersianDate.daysInMonth(year, month) shouldBe days
    }
  }

  test("persian date should be convert to local date") {
    val date = PersianDate.of(1401, 3, 5).toLocalDate

    date.getYear shouldBe 2022
    date.getMonth shouldBe MAY
    date.getDayOfMonth shouldBe 26
  }

  test("persian date should be convert to utc local date") {
    val date = PersianDate.of(1401, 3, 5).toLocalDate(ZoneOffset.UTC)

    date.getYear shouldBe 2022
    date.getMonth shouldBe MAY
    date.getDayOfMonth shouldBe 25
  }
