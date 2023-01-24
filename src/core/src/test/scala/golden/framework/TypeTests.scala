package golden.framework

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import java.lang.reflect.{ParameterizedType as JParameterizedType, Type as JType, WildcardType as JWildcardType}

class TypeTests extends AnyFunSuite with Matchers:

  test("of should create type literal properly") {
    val simple = Type.of[Boolean]
    val alias = Type.of[scala.Predef.String]
    val parameterized = Type.of[Map[Int, String]]
    val wildcardNoBound = Type.of[Option[?]].asInstanceOf[ParameterizedType].args.head
    val wildcardUpperBound = Type.of[Option[? <: Number]].asInstanceOf[ParameterizedType].args.head
    val wildcardLowerBound = Type.of[Option[? >: Serializable]].asInstanceOf[ParameterizedType].args.head
    val wildcardUpperLowerBound = Type.of[Option[? >: Number <: Serializable]].asInstanceOf[ParameterizedType].args.head
    val intersection = Type.of[Boolean & String]
    val union = Type.of[Boolean | String]

    simple shouldBe a [Type]

    alias shouldBe a [Type]

    parameterized shouldBe a [ParameterizedType]
    parameterized.asInstanceOf[ParameterizedType].args shouldBe Seq(Type.of[Int], Type.of[String])

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

    intersection shouldBe a [IntersectionType]
    intersection.asInstanceOf[IntersectionType].left shouldBe Type.of[Boolean]
    intersection.asInstanceOf[IntersectionType].right shouldBe Type.of[String]

    union shouldBe a [UnionType]
    union.asInstanceOf[UnionType].left shouldBe Type.of[Boolean]
    union.asInstanceOf[UnionType].right shouldBe Type.of[String]
  }

  test("getType should convert type to equivalent java type properly") {
    val simple = Type.of[Boolean].getType
    val param = Type.of[Map[Int, String]].getType
    val wildcardNoBound = Type.of[Option[?]].asInstanceOf[ParameterizedType].args.head.getType
    val wildcardUpperBound = Type.of[Option[? <: Number]].asInstanceOf[ParameterizedType].args.head.getType
    val wildcardLowerBound = Type.of[Option[? >: Serializable]].asInstanceOf[ParameterizedType].args.head.getType
    val wildcardUpperLowerBound = Type.of[Option[? >: Number <: Serializable]].asInstanceOf[ParameterizedType].args.head.getType
    val intersection = Type.of[Boolean & String].getType
    val union = Type.of[Boolean | String].getType

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

    intersection shouldBe classOf[Object]

    union shouldBe classOf[Object]
  }

  test("rawType should convert type to equivalent class type properly") {
    val simple = Type.of[Boolean].rawType
    val param = Type.of[Map[Int, String]].rawType
    val wildcard = Type.of[Option[?]].asInstanceOf[ParameterizedType].args.head.rawType
    val intersection = Type.of[Boolean & String].rawType
    val union = Type.of[Boolean | String].rawType

    simple shouldBe classOf[Boolean]
    param shouldBe classOf[Map[?, ?]]
    wildcard shouldBe classOf[Object]
    intersection shouldBe classOf[Object]
    union shouldBe classOf[Object]
  }

  test("of class should return Type from specified class") {
    val clsType = Type.of(classOf[String])

    clsType should be (typeOf[java.lang.String])
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

    optionType.isOption shouldBe true
    nonOptionType.isOption shouldBe false
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
