package golden.framework

import quoted.*

private[framework] object Macros:

  private type _Symbol = Any
  private type _TypeRepr = Any

  inline def getType[A]: TypeInfo = ${ getTypeImpl[A] }
  private def getTypeImpl[A: Type](using Quotes): Expr[TypeInfo] =
    typeToExpr(quotes.reflect.TypeRepr.of[A])

  inline def getFullNameOf[A](inline expression: A => ?): String = ${ getFullNameOf[A]('expression) }
  private def getFullNameOf[A](expression: Expr[A => ?])(using Quotes): Expr[String] = {
    import quotes.reflect.*

    def resolveTermName(term: Term): String = term match {
      case Apply(select: Select, _) => resolveTermName(select)
      case Apply(_, items: Iterable[Term]) => items.map(resolveTermName).mkString(".")
      case Select(Ident(_), name) => name
      case Select(applyOrSelect, name) => resolveTermName(applyOrSelect) + "." + name
      case Inlined(_, _, Block(List(DefDef(_, _, _, Some(applyOrSelect))), _)) => resolveTermName(applyOrSelect)
      case Inlined(_, _, inlined: Inlined) => resolveTermName(inlined)
      case Ident(name) => name
    }
    val name = resolveTermName(expression.asTerm)
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

  inline def getAnnotatedMembers[T, A]: Iterable[(String, TypeInfo, Iterable[A])] = ${ getAnnotatedMembersImpl[T, A] }
  private def getAnnotatedMembersImpl[T: Type, A: Type](using Quotes): Expr[Iterable[(String, TypeInfo, Iterable[A])]] = {
    import quotes.reflect.*

    val typeSymbol = TypeRepr.of[T].typeSymbol
    val annotationType = TypeRepr.of[A]

    def getAnnotations(symbol: Symbol): Seq[Term] = {
      val ctorParams = typeSymbol.primaryConstructor.paramSymss.headOption.toSeq.flatten
      (symbol.annotations ++ ctorParams.filter(_.name == symbol.name).flatMap(_.annotations))
        .filter(_.tpe <:< annotationType)
    }

    def getMemberType(member: Symbol): _TypeRepr = {
      member.tree match
        case ValDef(_, t, _) => t.tpe
        case DefDef(_, _, t, _) => t.tpe
    }

    def toExpr(member: Symbol, annotations: Seq[Term]): Expr[(String, TypeInfo, Iterable[A])] = {
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

  inline def getAnnotatedPackageTypes[T, A]: Iterable[(TypeInfo, Iterable[?])] =
    ${ getAnnotatedPackageTypesImpl[T, A] }
  private def getAnnotatedPackageTypesImpl[T: Type, A: Type](using Quotes): Expr[Iterable[(TypeInfo, Iterable[?])]] = {
    import quotes.reflect.*

    def symbolToExpr(symbol: Symbol): Expr[(TypeInfo, Iterable[?])] = {
      val annotations = symbol.annotations.map(_.asExpr)
      '{(${symbolToTypeExpr(symbol)}, ${Expr.ofSeq(annotations)})}
    }

    val symbols = getPackageTypes(TypeRepr.of[T].typeSymbol.owner).map(_.asInstanceOf[Symbol])
      .filterNot(_.isNoSymbol)
      .filter(_.annotations.exists(_.tpe <:< TypeRepr.of[A]))

    Expr.ofSeq(symbols.map(symbolToExpr))
  }

  private def symbolToTypeExpr(symbol: _Symbol)(using Quotes): Expr[TypeInfo] = {
    import quotes.reflect.*

    val name = symbol.asInstanceOf[Symbol].fullName
    val simpleName = symbol.asInstanceOf[Symbol].name
    val isAbstract = symbol.asInstanceOf[Symbol].flags.is(Flags.Abstract)
    val isEnum = symbol.asInstanceOf[Symbol].flags.is(Flags.Enum)
    val paramInfos = symbol.asInstanceOf[Symbol].typeMembers.filter(_.isTypeParam)
      .map(_ => typeToExpr(TypeRepr.of[scala.Nothing]))
    '{TypeInfo(
      ${Expr(name)},
      ${Expr(simpleName)},
      ${Expr(isAbstract)},
      ${Expr(isEnum)},
      ${Expr.ofSeq(paramInfos)})}
  }

  private def getPackageTypes(pack: _Symbol)(using Quotes): Seq[_Symbol] = {
    import quotes.reflect.*

    pack.asInstanceOf[Symbol].declaredTypes.flatMap {
      case pack if pack.isPackageDef => getPackageTypes(pack)
      case tpe if tpe.isClassDef => Seq(tpe)
      case _ => Nil
    }
  }

  private def typeToExpr(tpe: _TypeRepr)(using Quotes): Expr[TypeInfo] = {
    import quotes.reflect.*

    def toTypeExpr(tpe: TypeRepr, params: Seq[TypeRepr] = Nil): Expr[TypeInfo] = {
      val symbol = tpe.dealias.typeSymbol
      val className = symbol.fullName
      val simpleName = symbol.name
      val isAbstract = symbol.flags.is(Flags.Abstract)
      val isEnum = tpe <:< TypeRepr.of[scala.reflect.Enum]
      val paramInfos = params.map(typeToExpr)
      '{TypeInfo(
        ${Expr(className)},
        ${Expr(simpleName)},
        ${Expr(isAbstract)},
        ${Expr(isEnum)},
        ${Expr.ofSeq(paramInfos)})}
    }

    tpe.asInstanceOf[TypeRepr] match {
      case AppliedType(tpe, params) =>
        toTypeExpr(tpe, params)
      case TypeBounds(firstType, _) if firstType.typeSymbol == TypeRepr.of[Nothing].typeSymbol =>
        toTypeExpr(firstType)
      case other =>
        toTypeExpr(other)
    }
  }
