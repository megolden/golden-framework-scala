package golden.framework.time

import java.time.DayOfWeek.SATURDAY

object PersianCalendar:

  final val nativeDayNames = Seq(
    "شنبه", "یک‌شنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه"
  )

  final val nativeMonthNames = Seq(
    "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
    "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
  )

  final val nativeDigits = Seq("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")

  final val cultureName = "fa-IR"

  final val firstDayOfWeek = SATURDAY

  final val currencyDecimalSeparator = ","

  final val numberDecimalSeparator = "/"

  final val currencySymbol = "ریال"

  final val AMDesignator = "قبل‌ازظهر"
  final val PMDesignator = "بعدازظهر"

  final val altAMDesignator = "ق.ظ"
  final val altPMDesignator = "ب.ظ"

  final val dateSeparator = "/"
  final val timeSeparator = ":"

  def isLeapYear(year: Int): Boolean = {
    val rem = (25 * year + 11) % 33
    rem < 8
  }
