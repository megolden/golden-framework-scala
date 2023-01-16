package golden.framework.hibernate.udt

import java.sql.Types.VARCHAR

class OptionStringType extends BasicSingleColumnType[Option[String], String](
  classOf[Option[String]],
  VARCHAR,
  _.get,
  Some[String])
