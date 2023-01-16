package golden.framework.hibernate.udt

import java.sql.Types.DECIMAL

class BigIntType extends BasicSingleColumnType[BigInt, java.math.BigInteger](
  classOf[BigInt],
  DECIMAL,
  _.bigInteger,
  BigInt.apply)
