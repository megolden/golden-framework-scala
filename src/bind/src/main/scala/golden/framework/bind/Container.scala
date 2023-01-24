package golden.framework.bind

import golden.framework.{Type, typeOf}
import java.io.Closeable

trait Container extends Closeable:

  val isRoot: Boolean

  val root: Container

  val tags: Set[Any]

  def get(serviceType: Type): Any

  inline final def get[T]: T =
    get(typeOf[T]).asInstanceOf[T]

  inline final def getOption[T]: Option[T] =
    get(typeOf[Option[T]]).asInstanceOf[Option[T]]

  inline final def getAll[T]: List[T] =
    get(typeOf[List[T]]).asInstanceOf[List[T]]

  def createScope(additionalSetup: ContainerBuilder => ?, tags: Any*): Container

  final def createScope(tags: Any*): Container =
    createScope(_ => (), tags*)
