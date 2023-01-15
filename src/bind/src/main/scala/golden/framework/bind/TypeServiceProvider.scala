package golden.framework.bind

import golden.framework.Type
import java.lang.reflect.{Modifier, Constructor}
import javax.inject.Inject
import golden.framework.bind.inject
import golden.framework.OptionExtensions.tap

private class TypeServiceProvider private extends ServiceProvider:

  private var _implType: Type = _
  private var _provider: Container => Any = _

  override def implementationType: Type = _implType

  def this(tpe: Type) = {
    this()
    _implType = tpe
    val (ctor, params) = findConstructor(tpe, None)
    _provider = createProvider(ctor, params)
  }

  def this(tpe: Type, constructorParams: Seq[Type]) = {
    this()
    _implType = tpe
    val (ctor, _) = findConstructor(tpe, Some(constructorParams))
    _provider = createProvider(ctor, constructorParams)
  }

  override def get(injector: Container): Any =
    _provider.apply(injector)

  private def createProvider(constructor: Constructor[?], params: Seq[Type]): Container => Any = {
    injector =>
      val args = params.map(injector.get)
      constructor.newInstance(args*)
  }

  private def findConstructor(tpe: Type, params: Option[Seq[Type]]): (Constructor[?], Seq[Type]) = {
    val constructors = tpe.getRawType.getDeclaredConstructors
      .filter(ctor => params.map(_.map(_.getType)).forall(_ == ctor.getGenericParameterTypes.toSeq))
      .sortBy(ctor => Modifier.isPublic(ctor.getModifiers))(Ordering[Boolean].reverse)
    constructors
      .find(_.isAnnotationPresent(classOf[inject]))
      .orElse(constructors.find(_.isAnnotationPresent(classOf[Inject])))
      .orElse(constructors.headOption)
      .tap(_.trySetAccessible())
      .map(ctor => (ctor, ctor.getGenericParameterTypes.toSeq.map(Type.of)))
      .getOrElse { throw Exception(s"can't resolve suitable constructor for type: $tpe") }
  }
