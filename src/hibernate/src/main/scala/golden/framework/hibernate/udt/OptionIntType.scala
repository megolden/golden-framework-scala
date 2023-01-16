package golden.framework.hibernate.udt

import java.sql.Types.INTEGER

class OptionIntType extends BasicSingleColumnType[Option[Int], Int](
  classOf[Option[Int]],
  INTEGER,
  _.get,
  Some[Int])
