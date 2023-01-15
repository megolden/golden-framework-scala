package golden.framework

import java.lang.reflect.{Type as JType, ParameterizedType as JParameterizedType}

private class ParameterizedTypeImpl(symbolName: String, simpleName: String, val args: Seq[Type])
  extends TypeImpl(symbolName, simpleName)
    with ParameterizedType:

  override val name: String = symbolName + args.map(_.name).mkString("[", ",", "]")

  override protected def internalGetType: JType = {
    val rawType = getRawType
    new JParameterizedType {
      override def getActualTypeArguments: Array[JType] = args.map(_.getType).toArray
      override def getRawType: JType = rawType
      override def getOwnerType: JType = null
    }
  }
