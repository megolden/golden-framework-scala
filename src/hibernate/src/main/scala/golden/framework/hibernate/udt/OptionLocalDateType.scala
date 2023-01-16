package golden.framework.hibernate.udt

import java.sql.Types.DATE
import java.time.LocalDate
import java.sql.Date

class OptionLocalDateType extends BasicSingleColumnType[Option[LocalDate], Date](
  classOf[Option[LocalDate]],
  DATE,
  model => Date.valueOf(model.get),
  value => Some(value.toLocalDate))
