package golden.framework

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.PersianUtils.*

class PersianUtilsTests extends AnyFunSuite with Matchers:

  test("numberToText should converts number to text properly") {
    numberToText(0) shouldBe "صفر"
    numberToText(1) shouldBe "یک"
    numberToText(10) shouldBe "ده"
    numberToText(16) shouldBe "شانزده"
    numberToText(100) shouldBe "صد"
    numberToText(1_000) shouldBe "هزار"
    numberToText(1_000_000) shouldBe "یک میلیون"
    numberToText(10_560) shouldBe "ده هزار و پانصد و شصت"
    numberToText(10_060) shouldBe "ده هزار و شصت"
    numberToText(10_000) shouldBe "ده هزار"
    numberToText(10_100) shouldBe "ده هزار و صد"
    numberToText(5_200) shouldBe "پنج هزار و دویست"
    numberToText(1_050) shouldBe "هزار و پنجاه"
    numberToText(2_000_000) shouldBe "دو میلیون"
    numberToText(2_000_000_000) shouldBe "دو میلیارد"
    numberToText(1_001_050) shouldBe "یک میلیون و هزار و پنجاه"
    numberToText(2_001_001_050) shouldBe "دو میلیارد و یک میلیون و هزار و پنجاه"
  }

  test("numberToText with append one should converts number to text properly") {
    numberToText(100, true) shouldBe "یک\u200Cصد"
    numberToText(150, true) shouldBe "یک\u200Cصد و پنجاه"
    numberToText(1_000, true) shouldBe "یک\u200Cهزار"
    numberToText(1_050, true) shouldBe "یک\u200Cهزار و پنجاه"
    numberToText(1_401, true) shouldBe "یک\u200Cهزار و چهارصد و یک"
  }

  test("numberToText converts negative numbers to text properly") {
    numberToText(-1) shouldBe "منفی یک"
    numberToText(-100, true) shouldBe "منفی یک\u200Cصد"
  }

  test("toPersianDigits replaces latin digit chars with persian native digits") {
    "Digits: 0123456789".toPersianDigits shouldBe "Digits: ۰۱۲۳۴۵۶۷۸۹"
  }

  test("toLatinDigits replaces persian native digit chars with latin digits") {
    "Digits: ۰۱۲۳۴۵۶۷۸۹".toLatinDigits(alsoArabicDigits = false) shouldBe "Digits: 0123456789"
  }

  test("toLatinDigits replaces arabic native digit chars with latin digits") {
    "Digits: ٠١٢٣٤٥٦٧٨٩".toLatinDigits(alsoArabicDigits = true) shouldBe "Digits: 0123456789"
  }

  test("toPersianKafYeh replaces arabic letters kaf and yeh with persian kaf yeh") {
    "كي بود".toPersianKafYeh shouldBe "کی بود"
  }
