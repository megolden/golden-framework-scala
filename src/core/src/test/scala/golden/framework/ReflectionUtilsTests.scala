package golden.framework

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.Entry
import golden.framework.ReflectionUtils.*
import golden.framework.typeOf

class ReflectionUtilsTests extends AnyFunSuite with Matchers:

  test("getAnnotations should return class annotations with specified type") {
    val someAnnotations = getAnnotations[SomeWithAnotherAnnotatedClass, someAnnotation]

    someAnnotations should have size 1
  }

  test("getAnnotations should return all class annotations") {
    val allAnnotations = getAnnotations[SomeWithAnotherAnnotatedClass]

    allAnnotations should have size 2
    allAnnotations.exists(_.isInstanceOf[someAnnotation]) shouldBe true
    allAnnotations.exists(_.isInstanceOf[anotherAnnotation]) shouldBe true
  }

  test("getPackageAnnotatedTypes should return all package annotated types with specified annotation class") {
    val types = getPackageAnnotatedTypes[SomeWithAnotherAnnotatedClass, anotherAnnotation]

    types should have size 2
    types.keySet shouldBe Set(typeOf[SomeWithAnotherAnnotatedClass], typeOf[AnotherAnnotatedClass])
    types(typeOf[SomeWithAnotherAnnotatedClass]).exists(_.isInstanceOf[someAnnotation]) shouldBe true
    types(typeOf[SomeWithAnotherAnnotatedClass]).exists(_.isInstanceOf[anotherAnnotation]) shouldBe true
    types(typeOf[AnotherAnnotatedClass]).exists(_.isInstanceOf[anotherAnnotation]) shouldBe true
  }

  test("getAnnotatedMembers should return all members annotated with specified annotation") {
    val members = getAnnotatedMembers[SomeClassWithAnnotatedMember, someAnnotation]

    members should have size 3
    members.keys should contain only("someValue", "someFunction", "someTwiceFunction")
    members("someValue")._2 should have size 1
    members("someValue")._2 shouldBe a [Iterable[someAnnotation]]
    members("someValue")._1 shouldBe typeOf[String]
    members("someFunction")._2 should have size 1
    members("someFunction")._2 shouldBe a [Iterable[someAnnotation]]
    members("someFunction")._1 shouldBe typeOf[String]
    members("someTwiceFunction")._2 should have size 2
    members("someTwiceFunction")._2 shouldBe a [Iterable[someAnnotation]]
    members("someTwiceFunction")._1 shouldBe typeOf[Int]
    members should not contain key ("anotherAnnotation")
  }

@someAnnotation
@anotherAnnotation
class SomeWithAnotherAnnotatedClass

@anotherAnnotation
class AnotherAnnotatedClass

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
