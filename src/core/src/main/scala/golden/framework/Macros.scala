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

  inline def annotatedMembers[A, B]: Iterable[(String, Iterable[B])] = ${ annotatedMembersImpl[A, B] }
  private def annotatedMembersImpl[A: Type, B: Type](using Quotes): Expr[Iterable[(String, Iterable[B])]] = {
    import quotes.reflect.*

    val symbol = TypeRepr.of[A].typeSymbol
    val annotationType = TypeRepr.of[B]

    val constructorParams = symbol.primaryConstructor.paramSymss.headOption.toSeq.flatten
    def getAnnotations(symbol: Symbol): Seq[Term] = {
      (symbol.annotations ++ constructorParams.filter(_.name == symbol.name).flatMap(_.annotations))
        .filter(_.tpe <:< annotationType)
    }

    def symbolToInfo(symbol: Symbol): Expr[(String, Iterable[B])] = {
      val name = symbol.name
      val annotations = getAnnotations(symbol).map(_.asExprOf[B])
      '{(${Expr(name)}, ${Expr.ofSeq(annotations)})}
    }

    val members = symbol.declarations.filter(getAnnotations(_).nonEmpty).map(symbolToInfo)

    Expr.ofSeq(members)
  }

  inline def getMembers[A]: Iterable[(String, TypeInfo)] = ${ getMembersImpl[A] }
  private def getMembersImpl[A: Type](using Quotes): Expr[Iterable[(String, TypeInfo)]] = {
    import quotes.reflect.*

    val symbol = TypeRepr.of[A].typeSymbol
    val members = symbol.fieldMembers ++ symbol.methodMembers.filter(_.paramSymss.flatten.isEmpty)

    def symbolToInfo(symbol: Symbol): Expr[(String, TypeInfo)] = {
      val name = symbol.name
      val tpe = symbol.tree match
        case ValDef(_, t, _) => t.tpe
        case DefDef(_, _, t, _) => t.tpe

      '{(${Expr(name)}, ${typeToTypeInfo(tpe)})}
    }

    Expr.ofSeq(members.map(symbolToInfo))
  }

  inline def getMembersWithAnnotations[A]: Iterable[(String, TypeInfo, Char, Iterable[?])] =
    ${ getMembersWithAnnotationsImpl[A] }
  private def getMembersWithAnnotationsImpl[A: Type](using Quotes)
    : Expr[Iterable[(String, TypeInfo, Char, Iterable[?])]] = {
    import quotes.reflect.*

    getMembersWithAnnotations(TypeRepr.of[A].typeSymbol)
  }

  inline def getAnnotatedPackageTypes[T, A]: Iterable[(TypeInfo, Iterable[A])] =
    ${ getAnnotatedPackageTypesImpl[T, A] }
  private def getAnnotatedPackageTypesImpl[T: Type, A: Type](using Quotes): Expr[Iterable[(TypeInfo, Iterable[A])]] = {
    import quotes.reflect.*

    def symbolToInfo(symbol: Symbol): Expr[(TypeInfo, Iterable[A])] = {
      val annotations = symbol.annotations.filter(_.tpe <:< TypeRepr.of[A]).map(_.asExprOf[A])
      '{(${symbolToTypeInfo(symbol)}, ${Expr.ofSeq(annotations)})}
    }

    val symbols = getPackageTypes(TypeRepr.of[T].dealias.typeSymbol.owner)
      .map(_.asInstanceOf[Symbol])
      .filterNot(_.isNoSymbol)
      .filter(_.annotations.exists(_.tpe <:< TypeRepr.of[A]))

    Expr.ofSeq(symbols.map(symbolToInfo))
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

  private def getPackageTypes(pack: _Symbol)(using Quotes): Seq[_Symbol] = {
    import quotes.reflect.*

    pack.asInstanceOf[Symbol].declaredTypes.flatMap {
      case pack if pack.isPackageDef => getPackageTypes(pack)
      case tpe if tpe.isClassDef => Seq(tpe)
      case _ => Nil
    }
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

  private def getMembersWithAnnotations(symbol: _Symbol)(using Quotes)
  : Expr[Iterable[(String, TypeInfo, Char /* M: method, F: field */, Iterable[?])]] = {
    import quotes.reflect.*

    val members = symbol.asInstanceOf[Symbol].fieldMembers ++
      symbol.asInstanceOf[Symbol].methodMembers.filter(_.paramSymss.flatten.isEmpty)

    val constructorParams = symbol.asInstanceOf[Symbol].primaryConstructor.paramSymss.headOption.toSeq.flatten
    def getAnnotations(symbol: Symbol): Seq[Term] =
      (symbol.annotations ++ constructorParams.filter(_.name == symbol.name).flatMap(_.annotations))
        .filterNot(_.tpe.typeSymbol == TypeRepr.of[scala.annotation.internal.SourceFile].typeSymbol)

    def symbolToInfo(symbol: Symbol): Expr[(String, TypeInfo, Char, Iterable[?])] = {
      val name = symbol.name
      val kind = if symbol.flags.is(Flags.Method) then 'M' else 'F'
      val annotations = getAnnotations(symbol).map(_.asExpr)
      val tpe = symbol.tree match
        case ValDef(_, t, _) => t.tpe
        case DefDef(_, _, t, _) => t.tpe

      '{(${Expr(name)}, ${typeToTypeInfo(tpe)}, ${Expr(kind)}, ${Expr.ofSeq(annotations)})}
    }

    Expr.ofSeq(members.map(symbolToInfo))
  }
