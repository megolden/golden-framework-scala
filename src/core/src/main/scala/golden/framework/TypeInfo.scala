package golden.framework

import golden.framework
import java.lang.reflect.Modifier

final class TypeInfo(
  val symbolName: String,
  val simpleName: String,
  val isAbstract: Boolean,
  val isEnum: Boolean,
  val args: Seq[TypeInfo] = Nil)
  extends Serializable:

  val name: String = {
    symbolName +
    (if args.nonEmpty then args.map(_.name).mkString("[", ",", "]") else "")
  }

  override def toString: String = name

  override def hashCode: Int = name.hashCode

  override def equals(obj: Any): Boolean = obj match {
    case that: TypeInfo => this.name == that.name
    case _ => false
  }

  def withArgs(types: TypeInfo*): TypeInfo =
    TypeInfo(symbolName, simpleName, isAbstract, isEnum, types)

  def asClass: Class[?] = getTypeClass(symbolName)

  private def getTypeClass(name: String): Class[?] = name match {
    case "scala.Byte" => classOf[Byte]
    case "scala.Short" => classOf[Short]
    case "scala.Int" => classOf[Int]
    case "scala.Long" => classOf[Long]
    case "scala.Float" => classOf[Float]
    case "scala.Double" => classOf[Double]
    case "scala.Char" => classOf[Char]
    case "scala.Boolean" => classOf[Boolean]
    case "scala.Unit" => classOf[Unit]
    case other => Class.forName(other)
  }

object TypeInfo:
  def fromClass(clazz: Class[?]): TypeInfo = {
    val modifiers = clazz.getModifiers
    val isEnum = classOf[scala.reflect.Enum].isAssignableFrom(clazz)
    val isAbstract = Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)
    val args = clazz.getTypeParameters.map(_ => typeOf[Nothing])
    TypeInfo(clazz.getName, clazz.getSimpleName, isAbstract, isEnum, args)
  }
