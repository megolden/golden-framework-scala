package golden.framework

import quoted.*
import golden.framework.Type as FType

private object Macros:

  def typeOf[T](using tpe: Type[T], quotes: Quotes): Expr[FType] =
    new Macros(using quotes).typeOf(tpe)

  def fullNameOf(expr: Expr[?])(using quotes: Quotes): Expr[String] =
    new Macros(using quotes).fullNameOf(expr)

  def nameOf(expr: Expr[?])(using quotes: Quotes): Expr[String] =
    new Macros(using quotes).nameOf(expr)

  def annotationsOf[T, A](using tpe: Type[T], annType: Type[A], quotes: Quotes): Expr[Seq[A]] =
    '{ ${new Macros(using quotes).annotationsOf(tpe, annType)}.map(_.asInstanceOf[A]) }

  def annotatedTypesOf[T, A](using tpe: Type[T], annType: Type[A], quotes: Quotes): Expr[Map[FType, Seq[A]]] = '{
    ${ new Macros(using quotes).annotatedTypesOf(tpe, annType) }
      .map((t, as) => t -> as.map(_.asInstanceOf[A]))
      .toMap
  }

  def annotatedMembersOf[T, A](using tpe: Type[T], annType: Type[A], quotes: Quotes): Expr[Map[FType.Member, Seq[A]]] = '{
    ${ new Macros(using quotes).annotatedMembersOf(tpe, annType) }
      .map((t, as) => t -> as.map(_.asInstanceOf[A]))
      .toMap
  }

private class Macros(using quotes: Quotes):
  import quotes.*
  import quotes.reflect.*

  def typeOf(tpe: Type[?]): Expr[FType] = {
    TypeRepr.of(using tpe) match {
      case AppliedType(base, args) if args.nonEmpty =>
        val symbolName = Expr(base.typeSymbol.fullName)
        val clazz = clazzOf(base)
        val argExprs = Expr.ofSeq(args.map(t => typeOf(t.asType)))
        '{ new ParameterizedTypeImpl(${ symbolName }, ${ clazz }, ${ argExprs }) }
      case TypeBounds(low, hi) =>
        val l = typeOf(low.asType)
        val h = typeOf(hi.asType)
        '{ new WildcardTypeImpl(${ l }, ${ h }) }
      case AndType(left, right) =>
        val l = typeOf(left.asType)
        val r = typeOf(right.asType)
        '{ new IntersectionTypeImpl(${ l }, ${ r }) }
      case OrType(left, right) =>
        val l = typeOf(left.asType)
        val r = typeOf(right.asType)
        '{ new UnionTypeImpl(${ l }, ${ r }) }
      case other =>
        val symbolName = Expr(other.typeSymbol.fullName)
        val clazz = clazzOf(other)
        '{ new TypeImpl(${ symbolName }, ${ clazz }) }
    }
  }

  def annotationsOf(tpe: Type[?], annotationType: Type[?]): Expr[Seq[Any]] = {
    val typeSymbol = TypeRepr.of(using tpe).typeSymbol
    Expr.ofSeq(getAnnotationsOf(typeSymbol, annotationType).map(_.asExprOf[Any]))
  }

  def annotatedTypesOf(rootType: Type[?], annotationType: Type[?]): Expr[Map[FType, Seq[Any]]] = {
    val rootTypeSymbol = TypeRepr.of(using rootType).typeSymbol
    val rootPackageSymbol = rootTypeSymbol.owner

    if !rootPackageSymbol.isPackageDef then
      sys.error(s"type is not a package root type: ${rootTypeSymbol.fullName}")

    val types = Expr.ofSeq(
      getAnnotatedTypesOf(rootPackageSymbol, annotationType).map { tpe =>
        '{ ${typeOf(tpe)} -> ${annotationsOf(tpe, annotationType)} }
      })

    '{ ${types}.toMap }
  }

  def annotatedMembersOf(tpe: Type[?], annotationType: Type[?]): Expr[Map[FType.Member, Seq[Any]]] = {
    val typeSymbol = TypeRepr.of(using tpe).typeSymbol
    val annTypeSymbol = TypeRepr.of(using annotationType).typeSymbol
    val members = getMembers(typeSymbol).filter(memberHasAnnotation(_, annTypeSymbol))

    def toMemberExpr(symbol: Symbol): Expr[FType.Member] = {
      val name = Expr(symbol.name)
      val tpe =
        if symbol.isDefDef then typeOf(symbol.tree.asInstanceOf[DefDef].returnTpt.tpe.asType)
        else typeOf(symbol.tree.asInstanceOf[ValDef].tpt.tpe.asType)
      if symbol.isDefDef then '{ new FType.MethodImpl(${name}, ${tpe}) }
      else '{ new FType.FieldImpl(${name}, ${tpe}) }
    }

    val memberExprs = Expr.ofSeq(
      members.map { symbol =>
        '{ ${toMemberExpr(symbol)} -> ${ Expr.ofSeq(getMemberAnnotationsOf(symbol, annotationType).map(_.asExprOf[Any])) } }
      })

    '{ ${ memberExprs }.toMap }
  }

  private def clazzOf(tpe: TypeRepr): Expr[Class[?]] = {
    Literal(ClassOfConstant(tpe)).asExprOf[Class[?]]
  }

  private def fullNameOf(expr: Expr[?]): Expr[String] = {
    val fullName = resolveFullNameOf(expr.asTerm).getOrElse {
      throw UnsupportedOperationException(s"name of: ${expr.show}")
    }
    Expr(fullName)
  }

  private def nameOf(expr: Expr[?]): Expr[String] = {
    val fullName = resolveFullNameOf(expr.asTerm).getOrElse {
      throw UnsupportedOperationException(s"name of: ${expr.show}")
    }
    Expr(fullName.split('.').last)
  }

  private def resolveFullNameOf(term: Term): Option[String] = term match {
    case Ident(name) if name.matches("""^\_\$\d+$""") => None
    case _: Typed => None
    case Apply(term, args) =>
      val termName = term match
        case ident: Ident if ident.symbol == Symbol.requiredMethod("scala.Predef.augmentString") => None
        case _ => resolveFullNameOf(term)
      Some((termName ++ args.flatMap(resolveFullNameOf)).mkString(".")).filter(_.nonEmpty)
    case Select(term, name) =>
      Some((resolveFullNameOf(term) ++ Some(name)).mkString(".")).filter(_.nonEmpty)
    case Block(List(DefDef(_, _, _, Some(term))), _) => resolveFullNameOf(term)
    case Inlined(_, _, term) => resolveFullNameOf(term)
    case Ident(name) => Some(name)
  }

  private def getAnnotatedTypesOf(pack: Symbol, annotationType: Type[?]): Seq[Type[?]] = {
    val annType = TypeRepr.of(using annotationType)
    pack.declaredTypes.flatMap {
      case pkg if pkg.isPackageDef => getAnnotatedTypesOf(pkg, annotationType)
      case tpe if tpe.hasAnnotation(annType.typeSymbol) => Some(tpe.typeRef.asType)
      case _ => None
    }
  }

  private def getMembers(symbol: Symbol): Seq[Symbol] = {

    def isObjectOwner(member: Symbol): Boolean = {
      val owner = member.owner
      owner.fullName == "java.lang.Object" || owner.fullName == "scala.Any"
    }

    def isValidMember(member: Symbol): Boolean = {
      !isObjectOwner(member) &&
      !member.isClassConstructor &&
      !member.flags.is(Flags.Artifact) &&
      !member.flags.is(Flags.Synthetic) &&
      !member.flags.is(Flags.Macro) &&
      !member.flags.is(Flags.Implicit)
    }

    symbol.fieldMembers.filter(isValidMember)
    ++
    symbol.methodMembers.filter(isValidMember)
  }

  private def getAnnotationsOf(symbol: Symbol, annotationType: Type[?]): Seq[Term] = {
    val annType = TypeRepr.of(using annotationType)
    symbol.annotations
      .filterNot(_.tpe.typeSymbol == TypeRepr.of[scala.annotation.internal.SourceFile].typeSymbol)
      .filter(_.tpe <:< annType)
  }

  private def getMemberAnnotationsOf(member: Symbol, annotationType: Type[?]): Seq[Term] = {
    getAnnotationsOf(member, annotationType) ++
      Seq(member.owner.primaryConstructor)
        .filterNot(_.isNoSymbol)
        .flatMap(_.paramSymss.flatten)
        .filter(_.name == member.name)
        .flatMap(getAnnotationsOf(_, annotationType))
  }

  private def memberHasAnnotation(member: Symbol, annotationTypeSymbol: Symbol): Boolean = {
    if member.hasAnnotation(annotationTypeSymbol) then true
    else
      Seq(member.owner.primaryConstructor)
        .filterNot(_.isNoSymbol)
        .flatMap(_.paramSymss.flatten)
        .filter(_.name == member.name)
        .exists(_.hasAnnotation(annotationTypeSymbol))
  }

  // exposed methods

  type SYMBOL = Any
  type TERM = Any

  def findAnnotatedTypesOf(packageSymbol: SYMBOL, annotationType: Type[?]): Seq[Type[?]] =
    getAnnotatedTypesOf(packageSymbol.asInstanceOf[Symbol], annotationType)

  def findAnnotationsOfType(typeSymbol: SYMBOL, annotationType: Type[?]): Seq[TERM] =
    getAnnotationsOf(typeSymbol.asInstanceOf[Symbol], annotationType)
