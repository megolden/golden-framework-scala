package golden.framework

import com.fasterxml.jackson.databind.ObjectMapper
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.TypeUtils.*
import java.lang.reflect.{Type as JType, ParameterizedType as JParameterizedType, WildcardType as JWildcardType}

class TypeTests extends AnyFunSuite with Matchers:

  test("of should create type literal properly") {
    val simple = Type.of[Boolean]
    val param = Type.of[Map[Int, String]]
    val wildcardNoBound = Type.of[Option[?]].asInstanceOf[ParameterizedType].args.head
    val wildcardUpperBound = Type.of[Option[? <: Number]].asInstanceOf[ParameterizedType].args.head
    val wildcardLowerBound = Type.of[Option[? >: Serializable]].asInstanceOf[ParameterizedType].args.head
    val wildcardUpperLowerBound = Type.of[Option[? >: Number <: Serializable]].asInstanceOf[ParameterizedType].args.head

    simple shouldBe a [Type]
    simple.getRawType shouldBe classOf[Boolean]

    param shouldBe a [ParameterizedType]
    param.asInstanceOf[ParameterizedType].args shouldBe Seq(Type.of[Int], Type.of[String])

    wildcardNoBound shouldBe a [WildcardType]
    wildcardNoBound.asInstanceOf[WildcardType].hi shouldBe Type.of[Any]
    wildcardNoBound.asInstanceOf[WildcardType].low shouldBe Type.of[Nothing]

    wildcardUpperBound shouldBe a [WildcardType]
    wildcardUpperBound.asInstanceOf[WildcardType].hi shouldBe Type.of[Number]
    wildcardUpperBound.asInstanceOf[WildcardType].low shouldBe Type.of[Nothing]

    wildcardLowerBound shouldBe a [WildcardType]
    wildcardLowerBound.asInstanceOf[WildcardType].hi shouldBe Type.of[Any]
    wildcardLowerBound.asInstanceOf[WildcardType].low shouldBe Type.of[Serializable]

    wildcardUpperLowerBound shouldBe a [WildcardType]
    wildcardUpperLowerBound.asInstanceOf[WildcardType].hi shouldBe Type.of[Serializable]
    wildcardUpperLowerBound.asInstanceOf[WildcardType].low shouldBe Type.of[Number]
  }

  test("getType should convert type to equivalent java type properly") {
    val simple = Type.of[Boolean].getType
    val param = Type.of[Map[Int, String]].getType
    val wildcardNoBound = Type.of[Option[?]].asInstanceOf[ParameterizedType].args.head.getType
    val wildcardUpperBound = Type.of[Option[? <: Number]].asInstanceOf[ParameterizedType].args.head.getType
    val wildcardLowerBound = Type.of[Option[? >: Serializable]].asInstanceOf[ParameterizedType].args.head.getType
    val wildcardUpperLowerBound = Type.of[Option[? >: Number <: Serializable]].asInstanceOf[ParameterizedType].args.head.getType

    simple shouldBe a[JType]
    simple shouldBe classOf[Boolean]

    param shouldBe a [JParameterizedType]
    param.asInstanceOf[JParameterizedType].getActualTypeArguments shouldBe Seq(classOf[Int], classOf[String])

    wildcardNoBound shouldBe a [JWildcardType]
    wildcardNoBound.asInstanceOf[JWildcardType].getUpperBounds shouldBe Seq(classOf[Any])
    wildcardNoBound.asInstanceOf[JWildcardType].getLowerBounds shouldBe empty

    wildcardUpperBound shouldBe a [JWildcardType]
    wildcardUpperBound.asInstanceOf[JWildcardType].getUpperBounds shouldBe Seq(classOf[Number])
    wildcardUpperBound.asInstanceOf[JWildcardType].getLowerBounds shouldBe empty

    wildcardLowerBound shouldBe a [JWildcardType]
    wildcardLowerBound.asInstanceOf[JWildcardType].getUpperBounds shouldBe Seq(classOf[Any])
    wildcardLowerBound.asInstanceOf[JWildcardType].getLowerBounds shouldBe Seq(classOf[Serializable])

    wildcardUpperLowerBound shouldBe a [JWildcardType]
    wildcardUpperLowerBound.asInstanceOf[JWildcardType].getUpperBounds shouldBe Seq(classOf[Serializable])
    wildcardUpperLowerBound.asInstanceOf[JWildcardType].getLowerBounds shouldBe Seq(classOf[Number])
  }

  test("getRawType should convert type to equivalent class type properly") {
    val simple = Type.of[Boolean].getRawType
    val param = Type.of[Map[Int, String]].getRawType
    val wildcard = Type.of[Option[?]].asInstanceOf[ParameterizedType].args.head.getRawType

    simple shouldBe classOf[Boolean]
    param shouldBe classOf[Map[?, ?]]
    wildcard shouldBe classOf[Object]
  }

  test("of class should return TypeInfo from specified class") {
    val clsType = Type.of(classOf[String])

    clsType should be (typeOf[String])
  }

  test("of class should ignore type arguments") {
    val clsType = Type.of(classOf[List[String]])

    clsType shouldBe a [Type]
    clsType should not be a [ParameterizedType]
  }

  test("equals and hashCode should be based on name") {
    val t1 = typeOf[List[Int]]
    val t2 = typeOf[List[Int]]

    t1.equals(t2) shouldBe true
    t1.hashCode shouldBe t2.hashCode
  }

  test("isOptionType should return true when option type passed") {
    val optionType = typeOf[Option[?]]
    val nonOptionType = typeOf[String]

    optionType.isOptionType shouldBe true
    nonOptionType.isOptionType shouldBe false
  }

  test("isAbstract should return true when abstract type passed") {
    val abstractType = typeOf[Option[?]]
    val nonAbstractType = typeOf[String]

    abstractType.isAbstract shouldBe true
    nonAbstractType.isAbstract shouldBe false
  }

  test("isEnum should return true when an enum type passed") {
    val enumType = typeOf[SomeColorEnum]
    val nonEnumType = typeOf[String]

    enumType.isEnum shouldBe true
    nonEnumType.isEnum shouldBe false
  }

enum SomeColorEnum { case Red }
