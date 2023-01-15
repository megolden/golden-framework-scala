package golden.framework.bind

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.typeOf
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.any

class ServiceProviderTests extends AnyFunSuite with Matchers:

  test("instance service provider should return same instance properly") {
    val provider = InstanceServiceProvider(typeOf[Int], 10)

    val instance = provider.get()

    instance shouldBe 10
  }

  test("factory service provider should work properly") {
    val container = mock(classOf[Container])
    when(container.get(typeOf[String])).thenReturn("Mehdi")
    val provider = FactoryServiceProvider(
      typeOf[String],
      container => s"Hello ${container.get(typeOf[String])}")

    val instance = provider.get(container)

    verify(container, times(1)).get(typeOf[String])
    instance shouldBe "Hello Mehdi"
  }

  test("type service provider should return an instance of service type") {
    val container = mock(classOf[Container])
    val provider = TypeServiceProvider(typeOf[FakeService])

    val instance = provider.get(container)

    instance shouldBe a [FakeService]
  }

  test("type service provider should using non public constructor") {
    val container = mock(classOf[Container])
    val provider = TypeServiceProvider(typeOf[FakeServicePrivateCtor])

    val instance = provider.get(container)

    instance shouldBe a [FakeServicePrivateCtor]
  }

  test("type service provider should using constructor with specified parameter types") {
    val container = mock(classOf[Container])
    when(container.get(typeOf[String])).thenReturn("Mehdi")
    val provider = TypeServiceProvider(typeOf[FakeServiceWithCtor], Seq(typeOf[String]))

    val instance = provider.get(container)

    instance shouldBe a [FakeServiceWithCtor]
    verify(container, times(1)).get(typeOf[String])
  }

  test("type service provider should using public constructor first, and then non public") {
    val container = mock(classOf[Container])
    when(container.get(typeOf[String])).thenReturn("some")
    val provider = TypeServiceProvider(typeOf[FakeServicePublicPrivateCtors])

    val instance = provider.get(container)

    instance shouldBe a [FakeServicePublicPrivateCtors]
    verify(container, times(1)).get(typeOf[String])
  }

  test("type service provider should return an instance of service type with annotated constructor") {
    val container = mock(classOf[Container])
    val provider = TypeServiceProvider(typeOf[FakeServiceWithAnnotatedCtor])

    val instance = provider.get(container)

    instance shouldBe a [FakeServiceWithAnnotatedCtor]
    verify(container, never()).get(any())
  }

class FakeService
class FakeServicePrivateCtor private()
class FakeServicePublicPrivateCtors private() {
  def this(code: String) = this()
}
class FakeServiceWithCtor(code: Int) {
  def this(name: String) = this(0)
}
class FakeServiceWithAnnotatedCtor(code: Int, name: String) {
  @inject def this() = this(0, "")
}
