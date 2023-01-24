package golden.framework.bind

import quoted.*
import golden.framework.Type as FType
import javax.inject.Inject

private object Macros:

  inline def findConstructorOf[T]: TypeFactory = ${ findConstructorOfImpl[T] }
  private def findConstructorOfImpl[T](using tpe: Type[T], quotes: Quotes): Expr[TypeFactory] =
    new Macros(using quotes).findConstructorOf(tpe)

  inline def findAnnotatedServicesOf[T]: Seq[(FType, TypeFactory, Seq[service])] = ${ findAnnotatedServicesOfImpl[T] }
  private def findAnnotatedServicesOfImpl[T](using tpe: Type[T], quotes: Quotes): Expr[Seq[(FType, TypeFactory, Seq[service])]] =
    new Macros(using quotes).findAnnotatedServicesOf(tpe)

private class Macros(using quotes: Quotes):
  import quotes.*
  import quotes.reflect.*

  private lazy val fMacros = new golden.framework.Macros(using quotes)

  def findConstructorOf(tpe: Type[?]): Expr[TypeFactory] = {
    val t = TypeRepr.of(using tpe)
    val constructor = resolveConstructorOf(t).getOrElse {
      throw Exception(s"can't find suitable constructor for type: ${t.show}")
    }
    val paramTypes = constructor.paramSymss.head
      .map(_.tree.asInstanceOf[ValDef].tpt.tpe.asType)
      .map(fMacros.typeOf)
    val newInstanceFun = createNewInstanceFunctionOf(t, constructor)
    '{ new TypeFactory {
      override val argumentTypes: Seq[FType] = ${Expr.ofSeq(paramTypes)}
      override def newInstance(args: Seq[Any]): Any = { ${newInstanceFun.asExprOf[Seq[Any] => Any]}.apply(args) }
    }}
  }

  def findAnnotatedServicesOf(rootType: Type[?]): Expr[Seq[(FType, TypeFactory, Seq[service])]] = {
    val annotationType = Type.of[service]
    val rootTypeSymbol = TypeRepr.of(using rootType).typeSymbol
    val rootPackageSymbol = rootTypeSymbol.owner

    if !rootPackageSymbol.isPackageDef then
      sys.error(s"type is not a package root type: ${rootTypeSymbol.fullName}")

    val types = fMacros.findAnnotatedTypesOf(rootPackageSymbol, annotationType)
    Expr.ofSeq(
      types.map { tpe =>
        val typeSymbol = TypeRepr.of(using tpe).typeSymbol
        val services = fMacros.findAnnotationsOfType(typeSymbol, annotationType).map(_.asInstanceOf[Term])
        '{ (${fMacros.typeOf(tpe)}, ${findConstructorOf(tpe)}, ${Expr.ofSeq(services.map(_.asExprOf[service]))}) }
      })
  }

  private def createNewInstanceFunctionOf(tpe: TypeRepr, constructor: Symbol): Block = {
    val paramTypes = constructor.paramSymss.head.map(_.tree.asInstanceOf[ValDef].tpt.tpe)
    val constructorTerm = New(Inferred(tpe)).select(constructor)
    Lambda(
      owner = Symbol.spliceOwner,
      tpe = MethodType(List("args"))(_ => List(TypeRepr.of[Seq[Any]]), _ => TypeRepr.of[Any]),
      rhsFn = (sym: Symbol, paramRefs: List[Tree]) => {
        val args = paramRefs.head.asExprOf[Seq[Any]].asTerm
        val argTerms = paramTypes.zipWithIndex.map { (argType, argIndex) =>
          val rawValue = Select.unique(args, "apply").appliedTo(Literal(IntConstant(argIndex)))
          Select.unique(rawValue, "asInstanceOf").appliedToType(argType)
        }
        val fn = Apply(constructorTerm, argTerms)
        fn.changeOwner(sym)
      })
  }

  private def resolveConstructorOf(tpe: TypeRepr): Option[Symbol] = {
    val ts = tpe.typeSymbol

    val constructors =
      ts.declarations.filter { sym =>
        sym.isClassConstructor &&
        !sym.isNoSymbol &&
        !sym.flags.is(Flags.Private) &&
        !sym.flags.is(Flags.Protected)
      }
      .sortBy(_ == ts.primaryConstructor)(Ordering[Boolean].reverse)

    constructors
      .find(_.hasAnnotation(TypeRepr.of[inject].typeSymbol))
      .orElse(constructors.find(_.hasAnnotation(TypeRepr.of[Inject].typeSymbol)))
      .orElse(constructors.headOption)
  }
