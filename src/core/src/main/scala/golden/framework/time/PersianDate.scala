package golden.framework.time

import com.ibm.icu.util.{Calendar, ULocale}
import java.time.{Clock, DateTimeException, DayOfWeek, Month}
import java.time.{Instant, LocalDate, LocalDateTime, LocalTime, OffsetDateTime, OffsetTime, ZoneId, ZoneOffset}
import java.time.chrono.{ChronoLocalDate, ChronoPeriod, Chronology}
import java.time.temporal.{ChronoUnit, Temporal, TemporalField, TemporalUnit}
import golden.framework.OptionExtensions.tap
import java.time.temporal.ChronoUnit.*
import java.time.temporal.ChronoField.*
import java.util.{Date, GregorianCalendar}
import PersianCalendar.{dateSeparator, isLeapYear}
import java.time.format.DateTimeParseException
import scala.util.matching.Regex.quote

final class PersianDate private(private val calendar: Calendar) extends Ordered[PersianDate] with Serializable:

  val year: Int = calendar.get(Calendar.YEAR)
  val month: PersianMonth = PersianMonth.fromValue(calendar.get(Calendar.MONTH) + 1)
  val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)
  val dayOfYear: Int = calendar.get(Calendar.DAY_OF_YEAR)
  val dayOfWeek: DayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) match {
    case Calendar.SUNDAY => DayOfWeek.SUNDAY
    case Calendar.MONDAY => DayOfWeek.MONDAY
    case Calendar.TUESDAY => DayOfWeek.TUESDAY
    case Calendar.WEDNESDAY => DayOfWeek.WEDNESDAY
    case Calendar.THURSDAY => DayOfWeek.THURSDAY
    case Calendar.FRIDAY => DayOfWeek.FRIDAY
    case Calendar.SATURDAY => DayOfWeek.SATURDAY
  }
  val isLeapYear: Boolean = PersianCalendar.isLeapYear(year)

  def get(field: TemporalField): Int = field match {
    case DAY_OF_WEEK => dayOfWeek.getValue
    case DAY_OF_MONTH => dayOfMonth
    case DAY_OF_YEAR => dayOfYear
    case MONTH_OF_YEAR => month.value
    case YEAR => year
    case _ => throw DateTimeException(s"field not supported: $field")
  }

  def `with`(field: TemporalField, newValue: Int): PersianDate = field match {
    case DAY_OF_MONTH => withDayOfMonth(newValue)
    case MONTH_OF_YEAR => withMonth(newValue)
    case YEAR => withYear(newValue)
    case _ => throw DateTimeException(s"field not supported: $field")
  }

  def withYear(year: Int): PersianDate =
    PersianDate(calendar.copy.set(year = year))

  def withMonth(month: Int): PersianDate =
    PersianDate(calendar.copy.set(month = month - 1))

  def withDayOfMonth(dayOfMonth: Int): PersianDate =
    PersianDate(calendar.copy.set(date = dayOfMonth))

  def plus(amountToAdd: Int, unit: TemporalUnit): PersianDate = unit match {
    case DAYS => plusDays(amountToAdd)
    case WEEKS => plusWeeks(amountToAdd)
    case MONTHS => plusMonths(amountToAdd)
    case YEARS => plusYears(amountToAdd)
    case DECADES => plusYears(amountToAdd * 10)
    case CENTURIES => plusYears(amountToAdd * 100)
    case MILLENNIA => plusYears(amountToAdd * 1000)
    case _ => throw DateTimeException(s"unit not supported: $unit")
  }

  def plusYears(yearsToAdd: Int): PersianDate =
    PersianDate(calendar.copy.add(year = yearsToAdd))

  def plusMonths(monthsToAdd: Int): PersianDate =
    PersianDate(calendar.copy.add(month = monthsToAdd))

  def plusWeeks(weeksToAdd: Int): PersianDate =
    plusDays(weeksToAdd * 7)

  def plusDays(daysToAdd: Int): PersianDate =
    PersianDate(calendar.copy.add(date = daysToAdd))

  def minus(amountToSubtract: Int, unit: TemporalUnit): PersianDate = unit match {
    case DAYS => minusDays(amountToSubtract)
    case WEEKS => minusWeeks(amountToSubtract)
    case MONTHS => minusMonths(amountToSubtract)
    case YEARS => minusYears(amountToSubtract)
    case DECADES => minusYears(amountToSubtract * 10)
    case CENTURIES => minusYears(amountToSubtract * 100)
    case MILLENNIA => minusYears(amountToSubtract * 1000)
    case _ => throw DateTimeException(s"unit not supported: $unit")
  }

  def minusYears(yearsToSubtract: Int): PersianDate =
    PersianDate(calendar.copy.add(year = math.negateExact(yearsToSubtract)))

  def minusMonths(monthsToSubtract: Int): PersianDate =
    PersianDate(calendar.copy.add(month = math.negateExact(monthsToSubtract)))

  def minusWeeks(weeksToSubtract: Int): PersianDate =
    minusDays(weeksToSubtract * 7)

  def minusDays(daysToSubtract: Int): PersianDate =
    PersianDate(calendar.copy.add(date = math.negateExact(daysToSubtract)))

  def atTime(hour: Int, minute: Int, second: Int): PersianDateTime =
    atTime(LocalTime.of(hour, minute, second))

  def atTime(hour: Int, minute: Int): PersianDateTime =
    atTime(LocalTime.of(hour, minute))

  def atTime(hour: Int, minute: Int, second: Int, nanoOfSecond: Int): PersianDateTime =
    atTime(LocalTime.of(hour, minute, second, nanoOfSecond))

  def atTime(time: LocalTime): PersianDateTime =
    PersianDateTime.of(this, time)

  def atStartOfDay: PersianDateTime =
    atTime(LocalTime.MIDNIGHT)

  def toLocalDate: LocalDate =
    toLocalDate(ZoneId.systemDefault)

  def toLocalDate(zone: ZoneId): LocalDate =
    LocalDate.ofInstant(calendar.getTime.toInstant, zone)

  def toInstant: Instant =
    calendar.getTime.toInstant

  override def clone: AnyRef =
    PersianDate(calendar.copy)

  override def compare(that: PersianDate): Int =
    Option(that).map(_.calendar).map(calendar.compareTo).getOrElse(1)

  override def toString: String =
    f"$year%04d$dateSeparator${month.value}%02d$dateSeparator$dayOfMonth%02d"

  override def hashCode: Int =
    calendar.hashCode

  override def equals(obj: Any): Boolean = obj match {
    case that: PersianDate => calendar.equals(that.calendar)
    case _ => false
  }

object PersianDate:

  final val MinYear = math.negateExact(999_999)
  final val MaxYear = 999_999

  final val Min: PersianDate =
    of(MinYear, 1, 1)

  final val Max: PersianDate =
    of(MaxYear, 12, 29)

  def now: PersianDate =
    PersianDate(getPersianCalendar.set(hour = 0, minute = 0, second = 0, millisecond = 0))

  def of(year: Int, month: Int, dayOfMonth: Int): PersianDate = {
    validate(year, month, dayOfMonth)
    PersianDate(getPersianCalendar.set(year, month - 1, dayOfMonth, 0, 0, 0, 0))
  }

  def of(year: Int, month: PersianMonth, dayOfMonth: Int): PersianDate =
    of(year, month.value, dayOfMonth)

  def ofInstant(instant: Instant): PersianDate = {
    val calendar = getPersianCalendar
    calendar.setTime(Date.from(instant))
    calendar.set(hour = 0, minute = 0, second = 0, millisecond = 0)
    PersianDate(calendar)
  }

  def from(date: LocalDate, zone: ZoneId): PersianDate =
    ofInstant(date.atStartOfDay(zone).toInstant)

  def ofYearDay(year: Int, dayOfYear: Int): PersianDate = {
    val calendar = getPersianCalendar
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, 0)
    calendar.set(Calendar.DATE, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)
    PersianDate(calendar)
  }

  def parse(text: CharSequence): PersianDate = {
    val sep = quote(dateSeparator)
    val pattern = raw"^\s*(?<year>\d{1,6})$sep(?<month>\d\d?)$sep(?<day>\d\d?)".r
    pattern.findFirstMatchIn(text) map { m =>
      of(m.group("year").toInt, m.group("month").toInt, m.group("day").toInt)
    } getOrElse {
      throw DateTimeParseException(s"invalid input date format", text, 0)
    }
  }

  def daysInMonth(year: Int, month: Int): Int = {
    if month < 1 || month > 12 then
      throw DateTimeException(s"invalid month: $month")

    month match {
      case m if m <= 6 => 31
      case m if m <= 11 => 30
      case 12 => if isLeapYear(year) then 30 else 29
    }
  }

  private[time] def fromCalendar(calendar: Calendar): PersianDate =
    PersianDate(calendar)

  private[time] def validate(year: Int, month: Int, dayOfMonth: Int): Unit = {
    if year < MinYear || year > MaxYear || year == 0 ||
       month < 1 || month > 12 ||
       dayOfMonth < 1 || dayOfMonth > daysInMonth(year, month) then

      throw DateTimeException(s"invalid date: $year$dateSeparator$month$dateSeparator$dayOfMonth")
  }
