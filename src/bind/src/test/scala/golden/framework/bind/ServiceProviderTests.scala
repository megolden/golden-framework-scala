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

  test("type service provider should using public constructor to instantiate service") {
    val container = mock(classOf[Container])
    val provider = TypeServiceProvider(typeOf[FakeService])

    val instance = provider.get(container)

    instance shouldBe a [FakeService]
  }

  test("type service provider should ignore non public constructor to instantiate") {
    an [Exception] should be thrownBy TypeServiceProvider(typeOf[FakeServicePrivateCtor])
  }

  test("type service provider should using annotated constructor") {
    val container = mock(classOf[Container])
    val provider = TypeServiceProvider(typeOf[FakeServiceWithAnnotatedCtor])

    val instance = provider.get(container)

    instance shouldBe a [FakeServiceWithAnnotatedCtor]
    verify(container, never()).get(any())
  }

  test("type service provider should using specified TypeFactory to instantiate service") {
    val container = mock(classOf[Container])
    when(container.get(typeOf[Int])).thenReturn(10)
    when(container.get(typeOf[String])).thenReturn("some")
    val typeFactory = mock(classOf[TypeFactory])
    when(typeFactory.argumentTypes).thenReturn(Seq(typeOf[Int], typeOf[String]))
    when(typeFactory.newInstance(Seq(10, "some"))).thenReturn(new FakeServiceWithAnnotatedCtor(10, "some"))
    val provider = TypeServiceProvider(typeOf[FakeServiceWithAnnotatedCtor], typeFactory)

    val instance = provider.get(container)

    instance shouldBe a [FakeServiceWithAnnotatedCtor]
    verify(typeFactory, times(1)).newInstance(Seq(10, "some"))
  }

class FakeService
class FakeServicePrivateCtor private()
class FakeServicePublicPrivateCtors private() {
  def this(code: String) = this()
}
class FakeServiceWithCtor(val code: Int) {
  def this(name: String) = this(0)
}
class FakeServiceWithAnnotatedCtor(val code: Int, val name: String) {
  def this(code: Int) = this(code, "")
  @inject def this() = this(0, "")
  def this(name: String) = this(0, name)
}
