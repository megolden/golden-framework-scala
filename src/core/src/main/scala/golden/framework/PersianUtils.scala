package golden.framework

import scala.collection.mutable.ListBuffer
import golden.framework.BooleanUtils.{and, or}
import golden.framework.StringUtils.Empty

object PersianUtils:

  def numberToText(number: Long, appendOne: Boolean = false): String = {

    val NEGATE_SYMBOL = "منفی"
    val UNITS = Array(
      "صفر", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه",
      "ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده"
    )
    val TENS = Array(
      "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود"
    )
    val HUNDREDS = Array(
      "یک\u200Cصد",
      "صد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد"
    )
    val THOUSANDS = Array(
      "یک\u200Cهزار",
      "هزار", "میلیون", "میلیارد", "تریلیون", "کوآدریلیون", "کوینتیلیون"
    )

    def append(t1: String, t2: String, sep: String = " و "): String =
      if t2.isEmpty then t1 else {
        if t1.isEmpty then t2 else t1 + sep + t2
      }

    if number == 0L then return UNITS.head

    val isNegate = math.signum(number) == -1
    var num = if math.signum(number) == -1 then math.negateExact(number) else number
    var text = Empty
    var thPow = 0
    while (num != 0L) {

      val t = (num % 1000L).toInt match {
        case 0 => Empty
        case 1 if thPow == 1 => if appendOne then THOUSANDS.head else THOUSANDS(1)
        case value =>
          var n = value
          var t = Empty

          if (n >= 100) {
            if (n < 200 and appendOne) t = append(t, HUNDREDS.head)
            else t = append(t, HUNDREDS(n / 100))
            n %= 100
          }

          if (n >= 20) {
            t = append(t, TENS(n / 10 - 2))
            n %= 10
          }

          if (n == 1)
            t = append(t, UNITS(n))

          if (n > 1)
            t = append(t, UNITS(n))

          if (thPow > 0)
            if (thPow == 1 and appendOne) t = append(t, THOUSANDS.head, " ")
            else t = append(t, THOUSANDS(thPow), " ")

          t
      }

      text = append(t, text)
      num /= 1000
      thPow += 1
    }

    if isNegate then append(NEGATE_SYMBOL, text, " ") else text
  }

  extension (str: String)

    def toPersianDigits: String = {
      if str.isNull then str else str.map {
        case ch if ch.isDigit => (ch + 1728).toChar
        case other => other
      }
    }

    def toLatinDigits(alsoArabicDigits: Boolean = true): String = {
      if str.isNull then str else str.map {
        case ch if ch >= 1776 and ch <= 1785 => (ch - 1728).toChar
        case ch if alsoArabicDigits and ch >= 1632 and ch <= 1641 => (ch - 1584).toChar
        case other => other
      }
    }

    def toPersianKafYeh: String = {
      if str.isNull then str else str.map {
        case 'ك' => 'ک'
        case 'ي' => 'ی'
        case other => other
      }
    }
