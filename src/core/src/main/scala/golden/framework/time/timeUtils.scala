package golden.framework.time

import com.ibm.icu.util.{Calendar, ULocale}
import golden.framework.OptionExtensions.tap

private def getPersianCalendar = Calendar.getInstance(ULocale("fa_IR@calendar=persian"))

private def isLeapGregorianYear(year: Int): Boolean = {
  val gregorianCutoverYear = 1582
  if year >= gregorianCutoverYear then
    (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)) // Gregorian
  else
    year % 4 == 0 // Julian
}

extension (calendar: Calendar)

  private def copy: Calendar =
    calendar.clone.asInstanceOf[Calendar]

  private def set(
    year: Int = Int.MinValue,
    month: Int = Int.MinValue,
    date: Int = Int.MinValue,
    hour: Int = Int.MinValue,
    minute: Int = Int.MinValue,
    second: Int = Int.MinValue,
    millisecond: Int = Int.MinValue): Calendar = {

    if year != Int.MinValue then calendar.set(Calendar.YEAR, year)
    if month != Int.MinValue then calendar.set(Calendar.MONTH, month)
    if date != Int.MinValue then calendar.set(Calendar.DATE, date)
    if hour != Int.MinValue then calendar.set(Calendar.HOUR_OF_DAY, hour)
    if minute != Int.MinValue then calendar.set(Calendar.MINUTE, minute)
    if second != Int.MinValue then calendar.set(Calendar.SECOND, second)
    if millisecond != Int.MinValue then calendar.set(Calendar.MILLISECOND, millisecond)
    calendar
  }

  private def add(
    year: Int = 0,
    month: Int = 0,
    date: Int = 0,
    hour: Int = 0,
    minute: Int = 0,
    second: Int = 0,
    millisecond: Int = 0): Calendar = {

    if year != 0 then calendar.add(Calendar.YEAR, year)
    if month != 0 then calendar.add(Calendar.MONTH, month)
    if date != 0 then calendar.add(Calendar.DATE, date)
    if hour != 0 then calendar.add(Calendar.HOUR_OF_DAY, hour)
    if minute != 0 then calendar.add(Calendar.MINUTE, minute)
    if second != 0 then calendar.add(Calendar.SECOND, second)
    if millisecond != 0 then calendar.add(Calendar.MILLISECOND, millisecond)
    calendar
  }
