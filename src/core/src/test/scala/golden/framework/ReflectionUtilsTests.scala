package golden.framework

import golden.framework.ReflectionUtils.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

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

@someAnnotation
@anotherAnnotation
class SomeWithAnotherAnnotatedClass

@anotherAnnotation
class AnotherAnnotatedClass

class someAnnotation extends scala.annotation.StaticAnnotation
class anotherAnnotation extends scala.annotation.StaticAnnotation
