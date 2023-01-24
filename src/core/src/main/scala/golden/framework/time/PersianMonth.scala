package golden.framework.time

enum PersianMonth(val value: Int):
  case Farvardin extends PersianMonth(1)
  case Ordibehesht extends PersianMonth(2)
  case Khordad extends PersianMonth(3)
  case Tir extends PersianMonth(4)
  case Mordad extends PersianMonth(5)
  case Shahrivar extends PersianMonth(6)
  case Mehr extends PersianMonth(7)
  case Aban extends PersianMonth(8)
  case Azar extends PersianMonth(9)
  case Dey extends PersianMonth(10)
  case Bahman extends PersianMonth(11)
  case Esfand extends PersianMonth(12)

  val nativeName: String = PersianCalendar.nativeMonthNames(ordinal)
