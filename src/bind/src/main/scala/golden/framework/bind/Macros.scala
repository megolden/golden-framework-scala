package golden.framework.bind

import golden.framework.TypeInfo
import quoted.*
import golden.framework.bind.inject
import javax.inject.Inject

private[bind] object Macros:

  private type _Symbol = Any
  private type _TypeRepr = Any

  inline def findInjectableCtorParamTypes[A]: Seq[TypeInfo] = ${ findInjectableCtorParamTypesImpl[A] }
  private def findInjectableCtorParamTypesImpl[A: Type](using Quotes): Expr[Seq[TypeInfo]] = {
    import quotes.reflect.*

    val symbol = TypeRepr.of[A].typeSymbol
    findSymbolInjectableCtorParamTypes(symbol)
  }
  private def findSymbolInjectableCtorParamTypes(symbol: _Symbol)(using Quotes): Expr[Seq[TypeInfo]] = {
    import quotes.reflect.*

    val constructors = symbol.asInstanceOf[Symbol].declarations.filter(_.isClassConstructor).filter(isPublicSymbol)
    val primaryConstructor = Some(symbol.asInstanceOf[Symbol].primaryConstructor).filterNot(_.isNoSymbol).orNull
    val constructor = constructors
      .find(_.annotations.exists(_.tpe.typeSymbol == TypeRepr.of[inject].typeSymbol))
      .orElse(constructors.find(_.annotations.exists(_.tpe.typeSymbol == TypeRepr.of[Inject].typeSymbol)))
      .orElse(constructors.find(constructor => constructor == primaryConstructor))
      .orElse(constructors.headOption)
      .getOrElse { throw Exception(s"can't find suitable constructor on type '$symbol'") }
    val params = constructor.paramSymss.head
    val paramTypes = params.map(_.tree).collect {
      case ValDef(_, pType, _) => typeToTypeInfo(pType.tpe)
    }

    Expr.ofSeq(paramTypes)
  }

  inline def getAnnotatedPackageTypes[T, A]: Iterable[(TypeInfo, Iterable[A], Seq[TypeInfo])] =
    ${ getAnnotatedPackageTypesImpl[T, A] }
  private def getAnnotatedPackageTypesImpl[T: Type, A: Type](using Quotes)
    : Expr[Iterable[(TypeInfo, Iterable[A], Seq[TypeInfo])]] = {
    import quotes.reflect.*

    def symbolToInfo(symbol: Symbol): Expr[(TypeInfo, Iterable[A], Seq[TypeInfo])] = {
      val annotations = symbol.annotations.filter(_.tpe <:< TypeRepr.of[A]).map(_.asExprOf[A])
      val ctorParams = findSymbolInjectableCtorParamTypes(symbol)
      '{(${symbolToTypeInfo(symbol)}, ${Expr.ofSeq(annotations)}, ${ctorParams})}
    }

    val symbols = getPackageTypes(TypeRepr.of[T].typeSymbol.owner).map(_.asInstanceOf[Symbol])
      .filterNot(_.isNoSymbol)
      .filter(_.annotations.exists(_.tpe <:< TypeRepr.of[A]))

    Expr.ofSeq(symbols.map(symbolToInfo))
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

    tpe.asInstanceOf[TypeRepr] match
      case AppliedType(tpe, params) =>
        toTypeInfo(tpe, params)
      case TypeBounds(firstType, _) if firstType.typeSymbol == TypeRepr.of[Nothing].typeSymbol =>
        toTypeInfo(firstType)
      case other =>
        toTypeInfo(other)
  }

  private def isPublicSymbol(symbol: _Symbol)(using Quotes): Boolean = {
    import quotes.reflect.*

    !symbol.asInstanceOf[Symbol].flags.is(Flags.Private) && !symbol.asInstanceOf[Symbol].flags.is(Flags.Protected)
  }
