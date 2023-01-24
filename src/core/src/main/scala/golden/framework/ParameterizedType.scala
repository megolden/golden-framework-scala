package golden.framework

import java.lang.reflect.{ParameterizedType as JParameterizedType, Type as JType}

trait ParameterizedType extends Type:
  val args: Seq[Type]

  override def name: String = symbolName + args.map(_.name).mkString("[", ",", "]")

  override def getType: JType = {
    new JParameterizedType {
      override def getActualTypeArguments: Array[JType] = args.map(_.getType).toArray
      override def getRawType: JType = rawType
      override def getOwnerType: JType = throw UnsupportedOperationException("nested type not supported")
    }
  }
