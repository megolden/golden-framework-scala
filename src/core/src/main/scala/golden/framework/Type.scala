package golden.framework

import java.lang.reflect.Modifier
import java.lang.reflect.{ParameterizedType as JParameterizedType, Type as JType, WildcardType as JWildcardType}

trait Type:
  val symbolName: String
  val rawType: Class[?]
  final def isAbstract: Boolean = Modifier.isAbstract(rawType.getModifiers)
  final def isEnum: Boolean = classOf[scala.reflect.Enum].isAssignableFrom(rawType)
  final def isOption: Boolean = classOf[Option[?]].isAssignableFrom(rawType)
  def name: String = symbolName
  final def simpleName: String = rawType.getSimpleName
  def getType: JType = rawType
  final override def toString: String = name
  final override def hashCode: Int = name.hashCode
  final override def equals(obj: Any): Boolean = obj match {
    case that: Type => this.name == that.name
    case _ => false
  }

object Type:

  trait Member:
    val name: String
    val tpe: Type
    override def hashCode: Int = name.hashCode
    override def equals(obj: Any): Boolean = obj match {
      case that: Member => this.name == that.name
      case _ => false
    }
    override def toString: String = s"$name: $tpe"
  trait Field extends Member
  trait Method extends Member:
    override def toString: String = s"$name(): $tpe"

  def of(tpe: JType): Type = {
    def getRawType(tpe: JType): Class[?] = tpe match {
      case clazz: Class[?] => clazz
      case other => Class.forName(other.getTypeName)
    }

    tpe match {
      case param: JParameterizedType =>
        new ParameterizedTypeImpl(
          param.getRawType.getTypeName,
          getRawType(param.getRawType),
          param.getActualTypeArguments.map(of))
      case wildcard: JWildcardType =>
        new WildcardTypeImpl(
          wildcard.getLowerBounds.headOption.map(of),
          wildcard.getUpperBounds.headOption.map(of))
      case other =>
        new TypeImpl(other.getTypeName, getRawType(other))
    }
  }

  def of(clazz: Class[?]): Type = of(clazz.asInstanceOf[JType])

  import scala.quoted.*
  import golden.framework.Type as FType

  inline def of[T]: FType = ${ Macros.typeOf[T] }

  private[framework] class FieldImpl(val name: String, val tpe: FType) extends Field

  private[framework] class MethodImpl(val name: String, val tpe: FType) extends Method
