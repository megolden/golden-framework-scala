package golden.framework.hibernate.udt

import java.sql.Types.DOUBLE

class OptionDoubleType extends BasicSingleColumnType[Option[Double], Double](
  classOf[Option[Double]],
  DOUBLE, 
  _.get, 
  Some[Double])
