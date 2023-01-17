package golden.framework

import java.lang.reflect.{Type as JType, ParameterizedType as JParameterizedType}

trait ParameterizedType extends Type:
  val args: Seq[Type]

  override def name: String = symbolName + args.map(_.name).mkString("[", ",", "]")

  override protected def internalGetType: JType = {
    val rawType = getRawType
    new JParameterizedType {
      override def getActualTypeArguments: Array[JType] = args.map(_.getType).toArray
      override def getRawType: JType = rawType
      override def getOwnerType: JType = null
    }
  }
