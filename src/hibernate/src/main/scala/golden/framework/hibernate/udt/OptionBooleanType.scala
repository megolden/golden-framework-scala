package golden.framework.hibernate.udt

import java.sql.Types.BOOLEAN

class OptionBooleanType extends BasicSingleColumnType[Option[Boolean], Boolean](
  classOf[Option[Boolean]],
  BOOLEAN, 
  _.get, 
  Some[Boolean])
