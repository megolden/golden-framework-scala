package golden.framework

import quoted.*
import golden.framework.{Type as FType, TypeImpl, ParameterizedTypeImpl, WildcardTypeImpl}

private object Macros:

  private type _Symbol = Any
  private type _TypeRepr = Any
  private type _Term = Any

  inline def getType[T]: FType = ${ getTypeImpl[T] }
  private def getTypeImpl[T: Type](using Quotes): Expr[FType] =
    typeToExpr(quotes.reflect.TypeRepr.of[T])

  inline def getFullNameOf[A](inline expr: A): String = ${ getFullNameOfImpl[A]('{expr}) }
  private def getFullNameOfImpl[A](expr: Expr[A])(using Quotes): Expr[String] = {
    import quotes.reflect.*

    def resolveTermName(term: Term): Option[String] = term match {
      case Apply(term, args) =>
        Some((resolveTermName(term) ++ args.map(resolveTermName).flatten).mkString(".")).filter(_.nonEmpty)
      case Select(term, name) =>
        Some((resolveTermName(term) ++ Some(name)).mkString(".")).filter(_.nonEmpty)
      case Block(List(DefDef(_, _, _, Some(term))), _) => resolveTermName(term)
      case Inlined(_, _, term) => resolveTermName(term)
      case _: Typed => None
      case Ident(underscore) if underscore.matches("""^_\$\d+$""") => None
      case Ident(augment) if augment == "augmentString" => None
      case Ident(name) => Some(name)
    }
    val name = resolveTermName(expr.asTerm).getOrElse("")
    Expr(name)
  }

  trait Default[T] { var value: T }
  inline def getDefault[A]: A = ${ getDefaultImpl[A] }
  private def getDefaultImpl[A: Type](using Quotes): Expr[A] = {
      import quotes.reflect.*

      '{ new Default[A] { var value: A = _ }.value }
    }

  inline def getAnnotations[T, A]: Iterable[A] = ${ getAnnotationsImpl[T, A] }
  private def getAnnotationsImpl[T: Type, A: Type](using Quotes): Expr[Iterable[A]] = {
    import quotes.reflect.*

    val annotations =
      TypeRepr.of[T].typeSymbol.annotations
        .filter(_.tpe <:< TypeRepr.of[A])
        .map(_.asExprOf[A])

    Expr.ofSeq(annotations)
  }

  inline def getAnnotatedMembers[T, A]: Iterable[(String, FType, Iterable[A])] = ${ getAnnotatedMembersImpl[T, A] }
  private def getAnnotatedMembersImpl[T: Type, A: Type](using Quotes): Expr[Iterable[(String, FType, Iterable[A])]] = {
    import quotes.reflect.*

    val typeSymbol = TypeRepr.of[T].typeSymbol
    val annotationType = TypeRepr.of[A]

    def getAnnotations(symbol: Symbol): Seq[Term] = {
      val ctorParams = typeSymbol.primaryConstructor.paramSymss.headOption.toSeq.flatten
      (symbol.annotations ++ ctorParams.filter(_.name == symbol.name).flatMap(_.annotations))
        .filter(_.tpe <:< annotationType)
    }

    def getMemberType(member: Symbol): TypeRepr = {
      member.tree match
        case ValDef(_, t, _) => t.tpe
        case DefDef(_, _, t, _) => t.tpe
    }

    def toExpr(member: Symbol, annotations: Seq[Term]): Expr[(String, FType, Iterable[A])] = {
      val name = member.name
      val memberAnnotations = annotations.map(_.asExprOf[A])
      val tpe = getMemberType(member)
      '{(${Expr(name)}, ${typeToExpr(tpe)}, ${Expr.ofSeq(memberAnnotations)})}
    }

    val members = typeSymbol.declarations.map(member => (member, getAnnotations(member)))
      .collect { case (member, annotations) if annotations.nonEmpty => toExpr(member, annotations) }

    Expr.ofSeq(members)
  }

  inline def getAllAnnotations[T]: Iterable[?] = ${ getAllAnnotationsImpl[T] }
  private def getAllAnnotationsImpl[T: Type](using Quotes): Expr[Iterable[?]] = {
    import quotes.reflect.*

    val annotations =
      TypeRepr.of[T].typeSymbol.annotations
        .filterNot(_.tpe.typeSymbol == TypeRepr.of[scala.annotation.internal.SourceFile].typeSymbol)
        .map(_.asExpr)

    Expr.ofSeq(annotations)
  }

  inline def getAnnotatedPackageTypes[T, A]: Iterable[(FType, Iterable[A])] =
    ${ getAnnotatedPackageTypesImpl[T, A] }
  private def getAnnotatedPackageTypesImpl[T: Type, A: Type](using Quotes): Expr[Iterable[(FType, Iterable[A])]] = {
    import quotes.reflect.*

    def toExpr(tpe: TypeRepr, annotations: Seq[Term]): Expr[(FType, Iterable[A])] = {
      '{(${typeToExpr(tpe)}, ${Expr.ofSeq(annotations.map(_.asExprOf[A]))})}
    }

    Expr.ofSeq(
      getAnnotatedPackageTypes(TypeRepr.of[T].typeSymbol.maybeOwner, TypeRepr.of[A])
      .map((tpe, terms) => toExpr(tpe.asInstanceOf[TypeRepr], terms.map(_.asInstanceOf[Term]))))
  }

  def getAnnotatedPackageTypes(packageSymbol: _Symbol, annotationType: _TypeRepr)(using Quotes): Seq[(_TypeRepr, Seq[_Term])] = {
    import quotes.reflect.*

    val pkg = packageSymbol.asInstanceOf[Symbol]
    val annType = annotationType.asInstanceOf[TypeRepr]
    if pkg.isNoSymbol then return Nil
    pkg.declaredTypes.flatMap {
      case pkg if pkg.isPackageDef => getAnnotatedPackageTypes(pkg, annotationType)
      case tpe if tpe.hasAnnotation(annType.typeSymbol) =>
        Some((tpe.typeRef, tpe.annotations.filter(_.tpe <:< annType)))
      case _ => Nil
    }
  }

  def typeToExpr(tpe: _TypeRepr)(using Quotes): Expr[FType] = {
    import quotes.reflect.*

    val realType = tpe.asInstanceOf[TypeRepr].dealias.simplified
    val symbol = realType.typeSymbol
    realType match {
      case AppliedType(_, rawArgs) =>
        val args = rawArgs.map(typeToExpr)
        '{ new ParameterizedTypeImpl(
          ${ Expr(symbol.fullName) },
          ${ Expr(symbol.name) },
          ${ Expr.ofSeq(args) })
        }
      case TypeBounds(rawLow, rawHi) =>
        val low = typeToExpr(rawLow)
        val hi = typeToExpr(rawHi)
        '{ new WildcardTypeImpl(
          ${ Expr("?") },
          ${ Expr("?") },
          ${ low },
          ${ hi })
        }
      case _ =>
        '{ new TypeImpl(
          ${ Expr(symbol.fullName) },
          ${ Expr(symbol.name) })
        }
    }
  }
