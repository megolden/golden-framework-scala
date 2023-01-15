package golden.framework.bind

import quoted.*
import golden.framework.{Type as FType, Macros as FMacros}
import golden.framework.bind.inject
import javax.inject.Inject

private object Macros:

  private type _TypeRepr = Any

  inline def findInjectableCtorParams[A]: Seq[FType] = ${ findInjectableCtorParamsImpl[A] }
  private def findInjectableCtorParamsImpl[A: Type](using Quotes): Expr[Seq[FType]] =
    findSymbolInjectableCtorParams(quotes.reflect.TypeRepr.of[A])
  private def findSymbolInjectableCtorParams(tpe: _TypeRepr)(using Quotes): Expr[Seq[FType]] = {
    import quotes.reflect.*

    def isPublic(sym: Symbol): Boolean = {
      !sym.flags.is(Flags.Private) && !sym.flags.is(Flags.Protected)
    }

    val sym = tpe.asInstanceOf[TypeRepr].typeSymbol
    val primaryConstructor = Some(sym.primaryConstructor).filterNot(_.isNoSymbol).orNull
    val constructors = sym.declarations.filter(_.isClassConstructor)
      .sortBy(ctor => (ctor == primaryConstructor, isPublic(ctor)))(Ordering[(Boolean, Boolean)].reverse)
    val constructor = constructors
      .find(_.hasAnnotation(TypeRepr.of[inject].typeSymbol))
      .orElse(constructors.find(_.hasAnnotation(TypeRepr.of[Inject].typeSymbol)))
      .orElse(constructors.headOption)
      .getOrElse { throw Exception(s"can't find suitable constructor for type: $tpe") }
    val params = constructor.paramSymss.head
    val paramTypes = params.map(_.tree).collect {
      case ValDef(_, paramType, _) => FMacros.typeToExpr(paramType.tpe)
    }

    Expr.ofSeq(paramTypes)
  }

  inline def getAnnotatedPackageTypesWithCtor[T, A]: Iterable[(FType, Iterable[A], Seq[FType])] =
    ${ getAnnotatedPackageTypesWithCtorImpl[T, A] }
  private def getAnnotatedPackageTypesWithCtorImpl[T: Type, A: Type](using Quotes)
    : Expr[Iterable[(FType, Iterable[A], Seq[FType])]] = {
    import quotes.reflect.*

    def toExpr(tpe: TypeRepr, annotations: Seq[Term]): Expr[(FType, Iterable[A], Seq[FType])] = {
      '{(
        ${ FMacros.typeToExpr(tpe) },
        ${ Expr.ofSeq(annotations.map(_.asExprOf[A])) },
        ${ findSymbolInjectableCtorParams(tpe) })
      }
    }

    Expr.ofSeq(
      FMacros.getAnnotatedPackageTypes(TypeRepr.of[T].typeSymbol.maybeOwner, TypeRepr.of[A])
      .map((tpe, terms) => toExpr(tpe.asInstanceOf[TypeRepr], terms.map(_.asInstanceOf[Term]))))
  }
