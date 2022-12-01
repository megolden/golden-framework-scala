package golden.framework

import golden.framework.ReflectionUtils.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.Entry

class ReflectionUtilsTests extends AnyFunSuite with Matchers:

  test("getAnnotatedTypes should return annotated types") {
    val types = getAnnotatedTypes[Dummy3, dummy]

    types.keys should contain only (typeOf[Dummy1], typeOf[Dummy2])
    types(typeOf[Dummy1]) should have size 1
    types(typeOf[Dummy1]).head shouldBe a [dummy]
    types(typeOf[Dummy2]) should have size 2
    all (types(typeOf[Dummy2])) shouldBe a [dummy]
  }

  test("getMembersWithAnnotations should return type members with it's annotations") {
    val members = getMembersWithAnnotations[Dummy3]

    members.find(_.name == "sayHello").get shouldBe a [MemberDescriptor]
    members.find(_.name == "sayHello").get.annotations shouldBe empty
    members.find(_.name == "withAnnotation").get shouldBe a [MemberDescriptor]
    members.find(_.name == "withAnnotation").get.annotations should have size 1
  }

  @dummy class Dummy1
  @dummy @dummy class Dummy2
  class Dummy3(id: Int, name: String):
    def sayHello(): Unit = ()
    @dummy def withAnnotation(): Unit = ()
  class dummy extends annotation.StaticAnnotation
