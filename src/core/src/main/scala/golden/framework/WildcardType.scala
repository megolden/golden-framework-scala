package golden.framework

import java.lang.reflect.{Type as JType, WildcardType as JWildcardType}

trait WildcardType extends Type:
  val hi: Type
  val low: Type

  private val hasLow = low != Type.of[Nothing]
  private val hasHi = hi != Type.of[Any]

  override protected def internalGetRawType: Class[?] = classOf[Object]

  override def name: String = {
    if hasLow && hasHi then symbolName + " >: " + low.name + " <: " + hi.name
    else if hasLow then symbolName + " >: " + low.name
    else if hasHi then symbolName + " <: " + hi.name
    else symbolName
  }

  override protected def internalGetType: JType = {
    val upper = Option.when(hasHi)(hi).map(_.getType)
    val lower = Option.when(hasLow)(low).map(_.getType)
    new JWildcardType {
      override def getUpperBounds: Array[JType] = Array(upper.getOrElse(classOf[Object]))
      override def getLowerBounds: Array[JType] = lower.toArray
    }
  }
