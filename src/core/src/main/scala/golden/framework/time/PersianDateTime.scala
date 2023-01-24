package golden.framework.time

import com.ibm.icu.util.Calendar
import java.time.{DateTimeException, DayOfWeek, Instant, LocalDate, LocalDateTime, LocalTime, ZoneId}
import java.time.temporal.{Temporal, TemporalAdjuster, TemporalAmount, TemporalField, TemporalUnit}
import java.time.chrono.{ChronoLocalDate, ChronoLocalDateTime, ChronoPeriod, ChronoZonedDateTime, Chronology}
import golden.framework.OptionExtensions.tap
import golden.framework.time.PersianCalendar.{dateSeparator, isLeapYear, timeSeparator}
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoField.*
import java.time.temporal.ChronoUnit.*
import java.util.Date
import scala.util.matching.Regex.quote
import java.time.format.DateTimeParseException
import golden.framework.time.CalendarUtils.*

final class PersianDateTime private(private val calendar: Calendar) extends Ordered[PersianDateTime] with Serializable:
  import PersianDateTime.nanoSecondSeparator

  private final val MicrosPerDay = 86_400_000_000L
  private final val MillsPerDay = 86_400_000L

  private val _date = PersianDate.fromCalendar(calendar)
  private val _time = LocalTime.of(
    calendar.get(Calendar.HOUR_OF_DAY),
    calendar.get(Calendar.MINUTE),
    calendar.get(Calendar.SECOND),
    calendar.get(Calendar.MILLISECOND) * 1000_000
  )

  val year: Int = _date.year
  val month: PersianMonth = _date.month
  val dayOfMonth: Int = _date.dayOfMonth
  val dayOfYear: Int = _date.dayOfYear
  val dayOfWeek: DayOfWeek = _date.dayOfWeek
  val isLeapYear: Boolean = _date.isLeapYear
  val hour: Int = _time.getHour
  val minute: Int = _time.getMinute
  val second: Int = _time.getSecond
  val millisecond: Int = _time.get(MILLI_OF_SECOND)

  def get(field: TemporalField): Int = field match {
    case DAY_OF_WEEK |
         DAY_OF_MONTH |
         DAY_OF_YEAR |
         MONTH_OF_YEAR |
         YEAR =>
      _date.get(field)
    case _ =>
      _time.get(field)
  }

  def getLong(field: TemporalField): Long = field match {
    case NANO_OF_DAY |
         MICRO_OF_DAY =>
      _time.getLong(field)
    case _ =>
      get(field)
  }

  def toPersianDate: PersianDate =
    _date

  def toLocalTime: LocalTime =
    _time

  def toLocalDateTime: LocalDateTime =
    LocalDateTime.ofInstant(calendar.getTime.toInstant, ZoneId.systemDefault)

  def toLocalDateTime(zone: ZoneId): LocalDateTime =
    LocalDateTime.ofInstant(calendar.getTime.toInstant, zone)

  def toInstant: Instant =
    calendar.getTime.toInstant

  def `with`(field: TemporalField, newValue: Long): PersianDateTime = field match {
    case chrono: ChronoField =>
      if chrono.isTimeBased
      then PersianDateTime.of(_date, _time.`with`(field, newValue))
      else PersianDateTime.of(_date.`with`(field, newValue.toInt), _time)
    case _ => throw DateTimeException(s"field not supported: $field")
  }

  def withYear(year: Int): PersianDateTime =
    PersianDateTime.of(_date.withYear(year), _time)

  def withMonth(month: Int): PersianDateTime =
    PersianDateTime.of(_date.withMonth(month), _time)

  def withDayOfMonth(dayOfMonth: Int): PersianDateTime =
    PersianDateTime.of(_date.withDayOfMonth(dayOfMonth), _time)

  def withHour(hour: Int): PersianDateTime =
    PersianDateTime.of(_date, _time.withHour(hour))

  def withMinute(minute: Int): PersianDateTime =
    PersianDateTime.of(_date, _time.withMinute(minute))

  def withSecond(second: Int): PersianDateTime =
    PersianDateTime.of(_date, _time.withSecond(second))

  def withNano(nanoOfSecond: Int): PersianDateTime =
    PersianDateTime.of(_date, _time.withNano(nanoOfSecond))

  def plus(amountToAdd: Long, unit: TemporalUnit): PersianDateTime = unit match {
    case NANOS => plusNanos(amountToAdd)
    case MICROS => plusDays((amountToAdd / MicrosPerDay).toInt).plusNanos((amountToAdd % MicrosPerDay) * 1000)
    case MILLIS => plusDays((amountToAdd / MillsPerDay).toInt).plusNanos((amountToAdd % MillsPerDay) * 1000_000)
    case SECONDS => plusSeconds(amountToAdd.toInt)
    case MINUTES => plusMinutes(amountToAdd.toInt)
    case HOURS => plusHours(amountToAdd.toInt)
    case HALF_DAYS => plusDays((amountToAdd / 256L).toInt).plusHours((amountToAdd % 256L).toInt * 12)
    case _: ChronoUnit => PersianDateTime.of(_date.plus(amountToAdd.toInt, unit), _time)
    case _ => throw DateTimeException(s"unit not supported: $unit")
  }

  def plusYears(years: Int): PersianDateTime =
    PersianDateTime.of(_date.plusYears(years), _time)

  def plusMonths(months: Int): PersianDateTime =
    PersianDateTime.of(_date.plusMonths(months), _time)

  def plusWeeks(weeks: Int): PersianDateTime =
    plusDays(weeks * 7)

  def plusDays(days: Int): PersianDateTime =
    PersianDateTime.of(_date.plusDays(days), _time)

  def plusHours(hours: Int): PersianDateTime =
    PersianDateTime(calendar.copy.add(hour = hours))

  def plusMinutes(minutes: Int): PersianDateTime =
    PersianDateTime(calendar.copy.add(minute = minutes))

  def plusSeconds(seconds: Int): PersianDateTime =
    PersianDateTime(calendar.copy.add(second = seconds))

  def plusNanos(nanos: Long): PersianDateTime =
    PersianDateTime(calendar.copy.add(millisecond = (nanos / 1000_000L).toInt))

  def minus(amountToSubtract: Long, unit: TemporalUnit): PersianDateTime =
    plus(math.negateExact(amountToSubtract), unit)

  def minusYears(years: Int): PersianDateTime =
    plusYears(math.negateExact(years))

  def minusMonths(months: Int): PersianDateTime =
    plusMonths(math.negateExact(months))

  def minusWeeks(weeks: Int): PersianDateTime =
    plusWeeks(math.negateExact(weeks))

  def minusDays(days: Int): PersianDateTime =
    plusDays(math.negateExact(days))

  def minusHours(hours: Int): PersianDateTime =
    plusHours(math.negateExact(hours))

  def minusMinutes(minutes: Int): PersianDateTime =
    plusMinutes(math.negateExact(minutes))

  def minusSeconds(seconds: Int): PersianDateTime =
    plusSeconds(math.negateExact(seconds))

  def minusNanos(nanos: Int): PersianDateTime =
    plusNanos(math.negateExact(nanos))

  override def clone: AnyRef =
    PersianDateTime(calendar.copy)

  override def compare(that: PersianDateTime): Int =
    Option(that).map(_.calendar).map(calendar.compareTo).getOrElse(1)

  override def toString: String = {
    _date.toString +
    "T" +
    f"$hour%02d$timeSeparator$minute%02d$timeSeparator$second%02d$nanoSecondSeparator$millisecond%03d"
  }

  override def hashCode: Int =
    calendar.hashCode

  override def equals(obj: Any): Boolean = obj match {
    case that: PersianDateTime => calendar.equals(that.calendar)
    case _ => false
  }

object PersianDateTime:

  private final val nanoSecondSeparator = "."

  final val Min: PersianDateTime =
    of(PersianDate.Min, LocalTime.MIN)

  final val Max: PersianDateTime =
    of(PersianDate.Max, LocalTime.MAX)

  def now: PersianDateTime =
    PersianDateTime(getPersianCalendar)

  def of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int, millisecond: Int): PersianDateTime = {
    validate(year, month, dayOfMonth, hour, minute, second, millisecond)
    PersianDateTime(getPersianCalendar.set(year, month - 1, dayOfMonth, hour, minute, second, millisecond))
  }

  def of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int): PersianDateTime =
    of(year, month, dayOfMonth, hour, minute, second, 0)

  def of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int): PersianDateTime =
    of(year, month, dayOfMonth, hour, minute, 0, 0)

  def of(year: Int, month: PersianMonth, dayOfMonth: Int, hour: Int, minute: Int): PersianDateTime =
    of(year, month.value, dayOfMonth, hour, minute)

  def of(year: Int, month: PersianMonth, dayOfMonth: Int, hour: Int, minute: Int, second: Int): PersianDateTime =
    of(year, month.value, dayOfMonth, hour, minute, second)

  def of(year: Int, month: PersianMonth, dayOfMonth: Int, hour: Int, minute: Int, second: Int, millisecond: Int): PersianDateTime =
    of(year, month.value, dayOfMonth, hour, minute, second, millisecond)

  def of(date: PersianDate, time: LocalTime): PersianDateTime =
    of(date.year, date.month, date.dayOfMonth, time.getHour, time.getMinute, time.getSecond, time.get(MILLI_OF_SECOND))

  def ofInstant(instant: Instant): PersianDateTime = {
    val calendar = getPersianCalendar
    calendar.setTime(Date.from(instant))
    PersianDateTime(calendar)
  }

  def from(time: LocalDateTime, zone: ZoneId): PersianDateTime =
    ofInstant(time.atZone(zone).toInstant)

  def parse(text: CharSequence): PersianDateTime = {
    val dateSep = quote(dateSeparator)
    val timeSep = quote(timeSeparator)
    val nanoSep = quote(nanoSecondSeparator)
    val regexPattern = (
      raw"^\s*(?<year>\d{1,4})$dateSep(?<month>\d\d?)$dateSep(?<day>\d\d?)" +
      raw"((T|\s)" +
      raw"(?<hour>\d\d?)$timeSep(?<minute>\d\d?)" +
      raw"($timeSep(?<second>\d\d?)($nanoSep(?<mill>\d{1,3}))?)?)?"
    ).r
    regexPattern.findFirstMatchIn(text) map { m =>
      of(
        m.group("year").toInt,
        m.group("month").toInt,
        m.group("day").toInt,
        Option(m.group("hour")).filterNot(_.isEmpty).map(_.toInt).getOrElse(0),
        Option(m.group("minute")).filterNot(_.isEmpty).map(_.toInt).getOrElse(0),
        Option(m.group("second")).filterNot(_.isEmpty).map(_.toInt).getOrElse(0),
        Option(m.group("mill")).filterNot(_.isEmpty).map(_.toInt).getOrElse(0))
    } getOrElse {
      throw DateTimeParseException(s"invalid input date format", text, 0)
    }
  }

  private def validate(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int, millisecond: Int): Unit = {
    PersianDate.validate(year, month, dayOfMonth)

    if hour < 0 || hour > 23 ||
       minute < 0 || minute > 59 ||
       second < 0 || second > 59 ||
       millisecond < 0 || millisecond > 999 then

      throw DateTimeException(
        s"invalid time: $hour$timeSeparator$minute$timeSeparator$second$nanoSecondSeparator$millisecond")
  }
