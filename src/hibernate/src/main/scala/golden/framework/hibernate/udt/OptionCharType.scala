package golden.framework.hibernate.udt

import java.sql.Types.CHAR

class OptionCharType extends BasicSingleColumnType[Option[Char], Char](
  classOf[Option[Char]],
  CHAR, 
  _.get, 
  Some[Char])
