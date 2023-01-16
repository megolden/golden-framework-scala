package golden.framework.hibernate.udt

import java.sql.Types.FLOAT

class OptionFloatType extends BasicSingleColumnType[Option[Float], Float](
  classOf[Option[Float]],
  FLOAT,
  _.get,
  Some[Float])
