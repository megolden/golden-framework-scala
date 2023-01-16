package golden.framework.hibernate.udt

import java.sql.Types.BIGINT

class OptionLongType extends BasicSingleColumnType[Option[Long], Long](
  classOf[Option[Long]],
  BIGINT,
  _.get,
  Some[Long])
