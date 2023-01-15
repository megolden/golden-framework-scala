package golden.framework

trait WildcardType extends Type:
  val hi: Type
  val low: Type

  final override def getRawType: Class[?] = classOf[Object]
