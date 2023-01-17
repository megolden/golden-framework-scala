package golden.framework

import java.lang.reflect.{Type as JType, ParameterizedType as JParameterizedType, WildcardType as JWildcardType}

trait Type:
  val symbolName: String
  def name: String = symbolName
  def simpleName: String = symbolName
  protected def internalGetRawType: Class[?] = Class.forName(symbolName)
  protected def internalGetType: JType = internalGetRawType
  final def getType: JType =
    getPrimitiveType.getOrElse(internalGetType)
  final def getRawType: Class[?] =
    getPrimitiveType.getOrElse(internalGetRawType)
  override def toString: String = name
  override def hashCode: Int = name.hashCode
  override def equals(obj: Any): Boolean = obj match {
    case that: Type => this.name == that.name
    case _ => false
  }
  private def getPrimitiveType: Option[Class[?]] = name match {
    case "scala.Byte" => Some(classOf[Byte])
    case "scala.Short" => Some(classOf[Short])
    case "scala.Int" => Some(classOf[Int])
    case "scala.Long" => Some(classOf[Long])
    case "scala.Float" => Some(classOf[Float])
    case "scala.Double" => Some(classOf[Double])
    case "scala.Char" => Some(classOf[Char])
    case "scala.Boolean" => Some(classOf[Boolean])
    case "scala.Unit" => Some(classOf[Unit])
    case "scala.Nothing" => Some(classOf[Nothing])
    case "scala.Null" => Some(classOf[Null])
    case "scala.Any" => Some(classOf[Any])
    case "scala.AnyVal" => Some(classOf[AnyVal])
    case "scala.AnyRef" => Some(classOf[AnyRef])
    case _ => None
  }

object Type:

  inline def of[T]: Type =
    Macros.getType[T]

  def of(tpe: JType): Type = tpe match {
    case param: JParameterizedType =>
      new ParameterizedTypeImpl(
        param.getRawType.getTypeName,
        param.getActualTypeArguments.map(of))
    case wildcard: JWildcardType =>
      new WildcardTypeImpl(
        wildcard.getLowerBounds.headOption.map(of).getOrElse(Type.of[Nothing]),
        wildcard.getUpperBounds.headOption.map(of).getOrElse(Type.of[Any]))
    case _ =>
      new TypeImpl(tpe.getTypeName)
  }

  def of(clazz: Class[?]): Type = of(clazz.asInstanceOf[JType])
