package golden.framework.hibernate.udt

import java.sql.Types.TIMESTAMP
import java.time.LocalDateTime
import java.sql.Timestamp

class OptionLocalDateTimeType extends BasicSingleColumnType[Option[LocalDateTime], Timestamp](
  classOf[Option[LocalDateTime]],
  TIMESTAMP,
  value => Timestamp.valueOf(value.get),
  value => Some(value.toLocalDateTime))
