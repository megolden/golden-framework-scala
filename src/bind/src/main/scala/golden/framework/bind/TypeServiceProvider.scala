package golden.framework.bind

import golden.framework.Type
import java.lang.reflect.{Modifier, Constructor}
import javax.inject.Inject
import golden.framework.OptionExtensions.tap

class TypeServiceProvider(tpe: Type, factory: TypeFactory) extends ServiceProvider:

  override def implementationType: Type = tpe

  def this(tpe: Type) = this(tpe, TypeServiceProvider.createConstructorFor(tpe))

  override def get(injector: Container): Any = {
    val args = factory.argumentTypes.map(injector.get)
    factory.newInstance(args)
  }

private object TypeServiceProvider:

  def createConstructorFor(tpe: Type): TypeFactory = {
    val constructors = tpe.rawType.getDeclaredConstructors.filter(ctor => Modifier.isPublic(ctor.getModifiers))
    val constructor = constructors
      .find(_.isAnnotationPresent(classOf[inject]))
      .orElse(constructors.find(_.isAnnotationPresent(classOf[Inject])))
      .orElse(constructors.headOption)
      .getOrElse { throw Exception(s"can't resolve suitable constructor for type: $tpe") }
    new TypeFactory {
      override val argumentTypes: Seq[Type] = constructor.getGenericParameterTypes.map(Type.of)
      override def newInstance(args: Seq[Any]): Any = constructor.newInstance(args*)
    }
  }
