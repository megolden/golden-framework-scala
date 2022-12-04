package golden.framework.time

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import PersianMonth.*

class PersianMonthTests extends AnyFunSuite with Matchers:

  test("value should return value of month") {
    Farvardin.value shouldBe 1
    Ordibehesht.value shouldBe 2
    Khordad.value shouldBe 3
    Tir.value shouldBe 4
    Mordad.value shouldBe 5
    Shahrivar.value shouldBe 6
    Mehr.value shouldBe 7
    Aban.value shouldBe 8
    Azar.value shouldBe 9
    Dey.value shouldBe 10
    Bahman.value shouldBe 11
    Esfand.value shouldBe 12
  }

  test("ordinal should return ordinal of month") {
    Farvardin.ordinal shouldBe 0
    Ordibehesht.ordinal shouldBe 1
    Khordad.ordinal shouldBe 2
    Tir.ordinal shouldBe 3
    Mordad.ordinal shouldBe 4
    Shahrivar.ordinal shouldBe 5
    Mehr.ordinal shouldBe 6
    Aban.ordinal shouldBe 7
    Azar.ordinal shouldBe 8
    Dey.ordinal shouldBe 9
    Bahman.ordinal shouldBe 10
    Esfand.ordinal shouldBe 11
  }

  test("nativeName should return native name of month") {
    Farvardin.nativeName shouldBe "فروردین"
    Ordibehesht.nativeName shouldBe "اردیبهشت"
    Khordad.nativeName shouldBe "خرداد"
    Tir.nativeName shouldBe "تیر"
    Mordad.nativeName shouldBe "مرداد"
    Shahrivar.nativeName shouldBe "شهریور"
    Mehr.nativeName shouldBe "مهر"
    Aban.nativeName shouldBe "آبان"
    Azar.nativeName shouldBe "آذر"
    Dey.nativeName shouldBe "دی"
    Bahman.nativeName shouldBe "بهمن"
    Esfand.nativeName shouldBe "اسفند"
  }
