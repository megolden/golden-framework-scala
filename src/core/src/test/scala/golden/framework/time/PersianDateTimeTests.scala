package golden.framework.time

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.temporal.{ChronoField, ChronoUnit}
import java.time.*
import java.util.{Calendar, TimeZone}
import java.time.DayOfWeek.*
import PersianMonth.*

class PersianDateTimeTests extends AnyFunSuite with Matchers with TableDrivenPropertyChecks:

  test("persian date and time text should parse properly") {
    val time = PersianDateTime.parse("1399/12/9 23:5:6.123")

    time.year shouldBe 1399
    time.month shouldBe 12
    time.dayOfMonth shouldBe 9
    time.dayOfWeek shouldBe SATURDAY
    time.dayOfYear shouldBe 345
    time.nativeMonth shouldBe Esfand
    time.isLeapYear shouldBe true
    time.hour shouldBe 23
    time.minute shouldBe 5
    time.second shouldBe 6
    time.millisecond shouldBe 123
  }

  test("persian date and time partially text should parse properly") {
    val validDates = Table(("timeText", "year", "month", "day", "hour", "minute", "second", "millisecond"),
      ("1/1/1", 1, 1, 1, 0, 0, 0, 0),                   // without time
      ("1/1/1 13:5", 1, 1, 1, 13, 5, 0, 0),             // with time (hour and minute)
      ("1/1/1 13:5:50", 1, 1, 1, 13, 5, 50, 0),         // with time (hour, minute and second)
      ("1/1/1 13:5:50.680", 1, 1, 1, 13, 5, 50, 680),   // with full time
    )
    forAll(validDates) { (timeText: String, year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millisecond: Int) =>
      val time = PersianDateTime.parse(timeText)

      time.year shouldBe year
      time.month shouldBe month
      time.dayOfMonth shouldBe day
      time.hour shouldBe hour
      time.minute shouldBe minute
      time.second shouldBe second
      time.millisecond shouldBe millisecond
    }
  }

  test("parse should throw exception when invalid date and time text passed") {
    val invalidDateFormats = Table("dateText",
      "/1/1",                // without year
      "1234567/1/1",         // with exceeded year digits
      "1//1",                // without month
      "1/123/1",             // with exceeded month digits
      "1/1/"                 // without day
    )
    forAll(invalidDateFormats) { (dateText: String) =>
      a[DateTimeParseException] should be thrownBy {
        PersianDateTime.parse(dateText)
      }
    }
  }

  test("of should throw exception when invalid date or time value passed") {
    val invalidTimes = Table(("year", "month", "day", "hour", "minute", "second", "millisecond"),
      (-1000_000, 1, 1, 0, 0, 0, 0),  // with exceeded minimum year
      (1000_000, 1, 1, 0, 0, 0, 0),   // with exceeded maximum year
      (0, 1, 1, 0, 0, 0, 0),          // with zero year
      (1, 0, 1, 0, 0, 0, 0),          // with exceeded minimum month
      (1, 13, 1, 0, 0, 0, 0),         // with exceeded maximum month
      (1, 1, 0, 0, 0, 0, 0),          // with exceeded minimum day
      (1, 1, 32, 0, 0, 0, 0),         // with exceeded maximum day

      (1, 1, 1, -1, 0, 0, 0),         // with exceeded minimum year
      (1, 1, 1, 24, 0, 0, 0),         // with exceeded maximum year
      (1, 1, 1, 0, -1, 0, 0),         // with zero year
      (1, 1, 1, 0, 60, 0, 0),         // with exceeded minimum month
      (1, 1, 1, 0, 0, -1, 0),         // with exceeded maximum month
      (1, 1, 1, 0, 0, 60, 0),         // with exceeded minimum day
      (1, 1, 1, 0, 0, 0, -1),         // with exceeded maximum day
      (1, 1, 1, 0, 0, 0, 1000)        // with exceeded minimum day
    )
    forAll(invalidTimes) { (year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int, millisecond: Int) =>
      a[DateTimeException] should be thrownBy {
        PersianDateTime.of(year, month, day, hour, minute, second, millisecond)
      }
    }
  }
