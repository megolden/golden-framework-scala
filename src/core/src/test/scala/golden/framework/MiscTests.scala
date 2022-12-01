package golden.framework

import com.ibm.icu.text
import com.ibm.icu.util.{Calendar, ULocale}
import golden.framework.time.PersianDateTime
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import java.text.{DateFormatSymbols, SimpleDateFormat}
import java.time.LocalDate

class MiscTests extends AnyFunSuite with Matchers: // TODO: check me

  test("misc") {
    val cal = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"))
    val t = cal.getFirstDayOfWeek
    val cy = cal.get(Calendar.YEAR)
    val tt = cal.getTime
    val dt = LocalDate.now
    val cds = new text.DateFormatSymbols(new ULocale("fa_IR@calendar=persian")).getWeekdays

    val loc = new ULocale("fa_IR@calendar=persian").toLocale
    val cc = java.util.Calendar.getInstance(loc)
    val ds = new DateFormatSymbols(loc).getWeekdays


    val cct = cc.getFirstDayOfWeek
    val ccy = cc.get(java.util.Calendar.YEAR)

    val dtf = new SimpleDateFormat("")
    val pdtn = PersianDateTime.now
  }
