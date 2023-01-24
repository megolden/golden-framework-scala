package golden.framework

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.ReflectionUtils.*
import golden.framework.typeOf

class ReflectionUtilsTests extends AnyFunSuite with Matchers:

  test("annotationsOf should return class annotations with specified type") {
    val someAnnotations = annotationsOf[SomeWithAnotherAnnotatedClass, someAnnotation]

    someAnnotations should have size 1
    someAnnotations.forall(_.isInstanceOf[someAnnotation]) shouldBe true
  }

  test("annotationsOf should return all class annotations") {
    val allAnnotations = annotationsOf[SomeWithAnotherAnnotatedClass]

    allAnnotations should have size 2
    allAnnotations.exists(_.isInstanceOf[someAnnotation]) shouldBe true
    allAnnotations.exists(_.isInstanceOf[anotherAnnotation]) shouldBe true
  }

  test("annotatedMembersOf should return all members annotated with specified annotation") {
    val members = annotatedMembersOf[SomeClassWithAnnotatedMember, someAnnotation]
    def member(name: String): Type.Member = {
      members.keys.find(_.name == name).get
    }
    def annotationsOf(name: String): Seq[someAnnotation] = {
      members.collectFirst { case (member, annotations) if member.name == name => annotations }.get
    }

    members should have size 3
    members.keys.map(_.name) should contain only ("someValue", "someFunction", "someTwiceFunction")

    member("someValue") shouldBe a [Type.Field]
    annotationsOf("someValue") should have size 1
    annotationsOf("someValue") shouldBe a [Iterable[someAnnotation]]
    member("someValue").tpe shouldBe typeOf[String]

    member("someFunction") shouldBe a [Type.Method]
    annotationsOf("someFunction") should have size 1
    annotationsOf("someFunction") shouldBe a [Iterable[someAnnotation]]
    member("someFunction").tpe shouldBe typeOf[String]

    member("someTwiceFunction") shouldBe a [Type.Method]
    annotationsOf("someTwiceFunction") should have size 2
    annotationsOf("someTwiceFunction") shouldBe a [Iterable[someAnnotation]]
    member("someTwiceFunction").tpe shouldBe typeOf[Int]

    members.keys.map(_.name) should not contain "anotherAnnotation"
  }

  test("annotatedMembersOf should return all members annotated with specified annotation via primary constructor parameter") {
    val members = annotatedMembersOf[SomeClassWithParameterAnnotation, someAnnotation]
    val caseMembers = annotatedMembersOf[SomeCaseClassWithParameterAnnotation, someAnnotation]

    members should have size 1
    val (member, annotations) = members.head
    member.name shouldBe "someValue"
    member shouldBe a [Type.Field]
    annotations should have size 1
    annotations shouldBe a [Iterable[someAnnotation]]
    member.tpe shouldBe typeOf[String]

    caseMembers should have size 1
    val (caseMember, caseAnnotations) = caseMembers.head
    caseMember.name shouldBe "someValue"
    caseMember shouldBe a [Type.Field]
    caseAnnotations should have size 1
    caseAnnotations shouldBe a [Iterable[someAnnotation]]
    caseMember.tpe shouldBe typeOf[String]
  }

  test("annotatedTypesOf should return all package annotated types with specified annotation class") {
    val types = annotatedTypesOf[golden.framework.SomeWithAnotherAnnotatedClass, anotherAnnotation]

    types should have size 4
    types.keySet shouldBe Set(
      typeOf[SomeWithAnotherAnnotatedClass],
      typeOf[AnotherAnnotatedClass],
      typeOf[SomeAnnotatedEnum],
      typeOf[SomeAnnotatedTrait])
    types(typeOf[SomeWithAnotherAnnotatedClass]) should have size 1
    types(typeOf[AnotherAnnotatedClass]) should have size 1
    types(typeOf[SomeAnnotatedEnum]) should have size 1
    types(typeOf[SomeAnnotatedTrait]) should have size 1
  }

@someAnnotation
@anotherAnnotation
class SomeWithAnotherAnnotatedClass

@anotherAnnotation
class AnotherAnnotatedClass

@anotherAnnotation
enum SomeAnnotatedEnum { case Red }

@anotherAnnotation
trait SomeAnnotatedTrait

class someAnnotation extends scala.annotation.StaticAnnotation
class anotherAnnotation extends scala.annotation.StaticAnnotation

class SomeClassWithAnnotatedMember {
  @someAnnotation
  val someValue: String = ???

  @someAnnotation
  def someFunction: String = ???

  @someAnnotation
  @someAnnotation
  def someTwiceFunction: Int = ???

  @anotherAnnotation
  val anotherValue: Int = ???
}

class SomeClassWithParameterAnnotation(@someAnnotation val someValue: String)
case class SomeCaseClassWithParameterAnnotation(@someAnnotation someValue: String)
