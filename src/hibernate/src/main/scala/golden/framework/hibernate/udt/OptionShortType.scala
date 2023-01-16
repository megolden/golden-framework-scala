package golden.framework.hibernate.udt

import java.sql.Types.SMALLINT

class OptionShortType extends BasicSingleColumnType[Option[Short], Short](
  classOf[Option[Short]],
  SMALLINT,
  _.get,
  Some[Short])
