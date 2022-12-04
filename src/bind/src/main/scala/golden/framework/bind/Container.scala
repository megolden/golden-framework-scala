package golden.framework.bind

import golden.framework.TypeInfo
import golden.framework.typeOf
import java.io.Closeable

trait Container  extends Closeable:

  val isRoot: Boolean
  val root: Container
  val tags: Set[Any]

  def get(serviceType: TypeInfo): Any

  inline final def get[T]: T =
    get(typeOf[T]).asInstanceOf[T]

  def createScope(additionalSetup: ContainerBuilder => ?, tags: Any*): Container

  final def createScope(tags: Any*): Container =
    createScope(additionalSetup = (_: ContainerBuilder) => (), tags*)
