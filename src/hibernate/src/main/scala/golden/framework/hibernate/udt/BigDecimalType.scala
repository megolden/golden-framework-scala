package golden.framework.hibernate.udt

import java.sql.Types.DECIMAL

class BigDecimalType extends BasicSingleColumnType[BigDecimal, java.math.BigDecimal](
  classOf[BigDecimal],
  DECIMAL, 
  _.bigDecimal, 
  BigDecimal.apply)
