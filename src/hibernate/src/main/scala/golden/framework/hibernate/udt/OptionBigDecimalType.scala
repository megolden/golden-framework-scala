package golden.framework.hibernate.udt

import java.sql.Types.DECIMAL

class OptionBigDecimalType extends BasicSingleColumnType[Option[BigDecimal], java.math.BigDecimal](
  classOf[Option[BigDecimal]],
  DECIMAL, 
  _.get.bigDecimal, 
  decimal => Some(BigDecimal(decimal)))
