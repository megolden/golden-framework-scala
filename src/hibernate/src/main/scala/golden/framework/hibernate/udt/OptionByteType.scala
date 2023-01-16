package golden.framework.hibernate.udt

import java.sql.Types.TINYINT

class OptionByteType extends BasicSingleColumnType[Option[Byte], Byte](
  classOf[Option[Byte]],
  TINYINT, 
  _.get, 
  Some[Byte])
