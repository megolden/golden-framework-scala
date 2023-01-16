package golden.framework.hibernate.udt

import java.sql.Types.TIME
import java.time.LocalTime
import java.sql.Time

class OptionLocalTimeType extends BasicSingleColumnType[Option[LocalTime], Time](
  classOf[Option[LocalTime]],
  TIME,
  value => Time.valueOf(value.get),
  value => Some(value.toLocalTime))
