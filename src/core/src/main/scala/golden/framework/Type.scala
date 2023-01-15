package golden.framework

import java.lang.reflect.{Type as JType, ParameterizedType as JParameterizedType, WildcardType as JWildcardType}

trait Type:
  val name: String
  val symbolName: String
  val simpleName: String
  protected def internalGetType: JType
  protected def internalGetRawType: Class[?]
  def getType: JType =
    getPrimitiveType.getOrElse(internalGetType)
  def getRawType: Class[?] =
    getPrimitiveType.getOrElse(internalGetRawType)
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
  override def toString: String = name
  override def hashCode: Int = name.hashCode
  override def equals(obj: Any): Boolean = obj match {
    case that: Type => this.name == that.name
    case _ => false
  }

object Type:

  inline def of[T]: Type =
    Macros.getType[T]

  def of(tpe: JType): Type = tpe match {
    case param: JParameterizedType =>
      new ParameterizedTypeImpl(
        param.getRawType.getTypeName,
        getSimpleName(param),
        param.getActualTypeArguments.map(of))
    case wildcard: JWildcardType =>
      new WildcardTypeImpl(
        wildcard.getTypeName,
        getSimpleName(wildcard),
        wildcard.getLowerBounds.headOption.map(of).getOrElse(Type.of[Nothing]),
        wildcard.getUpperBounds.headOption.map(of).getOrElse(Type.of[Any]))
    case _ =>
      new TypeImpl(tpe.getTypeName, getSimpleName(tpe))
  }

  def of(clazz: Class[?]): Type = of(clazz.asInstanceOf[JType])

  private def getSimpleName(tpe: JType): String = {
    tpe match {
      case clazz: Class[?] =>
        clazz.getSimpleName
      case _ =>
        var name = tpe.getTypeName
        var i = name.indexOf('<')
        if i >= 0 then name = name.substring(0, i)
        i = name.indexOf('[')
        if i >= 0 then name = name.substring(0, i)
        i = name.lastIndexOf('$')
        if i >= 0 then name = name.substring(i + 1)
        i = name.lastIndexOf('#')
        if i >= 0 then name = name.substring(i + 1)
        i = name.lastIndexOf(':')
        if i >= 0 then name = name.substring(i + 1)
        i = name.indexOf('.')
        if i >= 0 then name.substring(i + 1) else name
    }
  }
