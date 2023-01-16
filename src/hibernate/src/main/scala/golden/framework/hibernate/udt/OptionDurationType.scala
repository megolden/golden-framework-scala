package golden.framework.hibernate.udt

import java.sql.Types.BIGINT
import java.time.Duration

class OptionDurationType extends BasicSingleColumnType[Option[Duration], Long](
  classOf[Option[Duration]],
  BIGINT,
  model => model.get.toNanos,
  value => Some(Duration.ofNanos(value)))
