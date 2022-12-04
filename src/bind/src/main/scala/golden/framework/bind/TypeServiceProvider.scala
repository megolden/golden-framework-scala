package golden.framework.bind

import golden.framework.TypeInfo
import TypeServiceProvider.resolveConstructor
import java.lang.reflect.Modifier
import javax.inject.Inject

class TypeServiceProvider(tpe: TypeInfo, constructorParameters: Seq[TypeInfo]) extends ServiceProvider:

  override val implementationType: TypeInfo = tpe
  private val _provider = createProviderFromConstructor(tpe, constructorParameters)

  def this(tpe: TypeInfo) =
    this(tpe, resolveConstructor(tpe))

  override def get(injector: Container): Any = {
    _provider.apply(injector)
  }

  private def createProviderFromConstructor(tpe: TypeInfo, constructorParameters: Seq[TypeInfo]): Container => Any = {
    val constructor = tpe.asClass.getDeclaredConstructor(constructorParameters.map(_.asClass) *)
    injector =>
      val args = constructorParameters.map(injector.get(_))
      constructor.newInstance(args *)
  }

object TypeServiceProvider:

  inline def resolveConstructor[T](): Seq[TypeInfo] = {
    Macros.findInjectableCtorParamTypes[T]
  }

  private def resolveConstructor(tpe: TypeInfo): Seq[TypeInfo] = {
    val constructors = tpe.asClass.getDeclaredConstructors.filter(ctor => Modifier.isPublic(ctor.getModifiers))
      constructors
      .find(_.isAnnotationPresent(classOf[Inject]))
      .orElse(constructors.sortBy(_.getParameterCount)(Ordering[Int].reverse).headOption)
      .map(_.getParameterTypes.map(TypeInfo.fromClass))
      .getOrElse {
        throw Exception(s"can't resolve suitable constructor for type: $tpe")
      }
  }
