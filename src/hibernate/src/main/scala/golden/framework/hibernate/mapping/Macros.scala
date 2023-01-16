package golden.framework.hibernate.mapping

import quoted.*
import golden.framework.Type as FType
import golden.framework.domain.unique
import golden.framework.Macros.typeToExpr

private object Macros:

  inline def getProperties[A]: Iterable[(String, FType, Boolean, Boolean)] = ${ getPropertiesImpl[A] }
  private def getPropertiesImpl[A: Type](using Quotes): Expr[Iterable[(String, FType, Boolean, Boolean)]] = {
    import quotes.reflect.*

    val symbol = TypeRepr.of[A].typeSymbol
    val ctorParams = symbol.primaryConstructor.paramSymss.headOption.toSeq.flatten

    def hasUniqueAnnotation(member: Symbol): Boolean = {
      val uniqueSymbol = TypeRepr.of[unique].typeSymbol
      member.hasAnnotation(uniqueSymbol) ||
        ctorParams.find(_.name == member.name).exists(_.hasAnnotation(uniqueSymbol))
    }

    def getMemberType(member: Symbol): TypeRepr = {
      member.tree match
        case ValDef(_, t, _) => t.tpe
        case DefDef(_, _, t, _) => t.tpe
    }

    def toExpr(member: Symbol): Expr[(String, FType, Boolean, Boolean)] = {
      val name = member.name
      val isField = !member.flags.is(Flags.Method)
      val isUnique = hasUniqueAnnotation(member)
      val tpe = getMemberType(member)
      '{ (${ Expr(name) }, ${ typeToExpr(tpe) }, ${ Expr(isField) }, ${ Expr(isUnique) }) }
    }

    val members = (symbol.fieldMembers ++ symbol.methodMembers.filter(_.paramSymss.flatten.isEmpty))
      .filter(getMemberType(_).typeSymbol != TypeRepr.of[scala.Unit].typeSymbol)

    Expr.ofSeq(members.map(toExpr))
  }
