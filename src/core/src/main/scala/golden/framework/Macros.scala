package golden.framework

import quoted.*

private[framework] object Macros:

  private type _Symbol = Any
  private type _TypeRepr = Any

  inline def getTypeInfo[A]: TypeInfo = ${ getTypeInfoImpl[A] }
  private def getTypeInfoImpl[A: Type](using Quotes): Expr[TypeInfo] =
    typeToTypeInfo(quotes.reflect.TypeRepr.of[A])

  inline def getNameOf[A](inline expression: A => ?): String = ${ getNameOfImpl[A]('expression) }
  private def getNameOfImpl[A](expression: Expr[A => ?])(using Quotes): Expr[String] = {
    import quotes.reflect.*

    def resolveTerm(term: Term): String = term match {
      case Inlined(_, _, Block(List(DefDef(_, _, _, Some(Apply(Select(_, name), _)))), _)) => name
      case Inlined(_, _, Block(List(DefDef(_, _, _, Some(Select(_, name)))), _)) => name
      case Inlined(_, _, inlined: Inlined) => resolveTerm(inlined)
    }

    val name = resolveTerm(expression.asTerm)

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

    def symbolToInfo(symbol: Symbol): Expr[(TypeInfo, Iterable[?])] = {
      val annotations = symbol.annotations.map(_.asExpr)
      '{(${symbolToTypeInfo(symbol)}, ${Expr.ofSeq(annotations)})}
    }

    val symbols = getPackageTypes(TypeRepr.of[T].typeSymbol.owner).map(_.asInstanceOf[Symbol])
      .filterNot(_.isNoSymbol)
      .filter(_.annotations.exists(_.tpe <:< TypeRepr.of[A]))

    Expr.ofSeq(symbols.map(symbolToInfo))
  }

  private def symbolToTypeInfo(symbol: _Symbol)(using Quotes): Expr[TypeInfo] = {
    import quotes.reflect.*

    val name = symbol.asInstanceOf[Symbol].fullName
    val simpleName = symbol.asInstanceOf[Symbol].name
    val isAbstract = symbol.asInstanceOf[Symbol].flags.is(Flags.Abstract)
    val isEnum = symbol.asInstanceOf[Symbol].flags.is(Flags.Enum)
    val paramInfos = symbol.asInstanceOf[Symbol].typeMembers.filter(_.isTypeParam)
      .map(_ => typeToTypeInfo(TypeRepr.of[scala.Nothing]))
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

  private def typeToTypeInfo(tpe: _TypeRepr)(using Quotes): Expr[TypeInfo] = {
    import quotes.reflect.*

    def toTypeInfo(tpe: TypeRepr, params: Seq[TypeRepr] = Nil): Expr[TypeInfo] = {
      val symbol = tpe.dealias.typeSymbol
      val className = symbol.fullName
      val simpleName = symbol.name
      val isAbstract = symbol.flags.is(Flags.Abstract)
      val isEnum = tpe <:< TypeRepr.of[scala.reflect.Enum]
      val paramInfos = params.map(typeToTypeInfo)
      '{TypeInfo(
        ${Expr(className)},
        ${Expr(simpleName)},
        ${Expr(isAbstract)},
        ${Expr(isEnum)},
        ${Expr.ofSeq(paramInfos)})}
    }

    tpe.asInstanceOf[TypeRepr] match {
      case AppliedType(tpe, params) =>
        toTypeInfo(tpe, params)
      case TypeBounds(firstType, _) if firstType.typeSymbol == TypeRepr.of[Nothing].typeSymbol =>
        toTypeInfo(firstType)
      case other =>
        toTypeInfo(other)
    }
  }
