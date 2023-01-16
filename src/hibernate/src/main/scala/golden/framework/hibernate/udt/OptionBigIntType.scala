package golden.framework.hibernate.udt

import java.sql.Types.DECIMAL

class OptionBigIntType extends BasicSingleColumnType[Option[BigInt], java.math.BigInteger](
  classOf[Option[BigInt]],
  DECIMAL,
  _.get.bigInteger, 
  integer => Some(BigInt(integer)))
