package golden.framework.bind

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.typeOf
import org.mockito.Mockito.*
import org.mockito.ArgumentMatchers.any
import javax.inject.Inject

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

  test("type service provider should return an instance of service type with max parameter count constructor") {
    val container = mock(classOf[Container])
    when(container.get(typeOf[String])).thenReturn("Mehdi")
    val provider = TypeServiceProvider(typeOf[FakeServiceWithCtors])

    val instance = provider.get(container)

    instance shouldBe a [FakeServiceWithCtors]
    verify(container, times(1)).get(typeOf[String])
  }

  test("type service provider should return an instance of service type with specified constructor") {
    val container = mock(classOf[Container])
    when(container.get(typeOf[Seq[Int]])).thenReturn(Seq(1, 2, 3))
    when(container.get(typeOf[Seq[String]])).thenReturn(Seq("Mehdi", "Reza"))
    val provider = TypeServiceProvider(typeOf[FakeServiceWithCtor], Seq(typeOf[Seq[Int]], typeOf[Seq[String]]))

    val instance = provider.get(container)

    instance shouldBe a [FakeServiceWithCtor]
    verify(container, times(1)).get(typeOf[Seq[Int]])
    verify(container, times(1)).get(typeOf[Seq[String]])
  }

  test("type service provider should throw exception when no public constructor found") {
    an [Exception] should be thrownBy {
      new TypeServiceProvider(typeOf[FakeServicePrivateCtor])
    }
  }

  test("type service provider should return an instance of service type with annotated constructor") {
    val container = mock(classOf[Container])
    val provider = TypeServiceProvider(typeOf[FakeServiceWithAnnotatedCtor])

    val instance = provider.get(container)

    instance shouldBe a [FakeServiceWithAnnotatedCtor]
    verify(container, never()).get(any())
  }

class FakeService
class FakeServiceWithCtors(name: String) { def this() = this("NoName") }
class FakeServiceWithCtor(codes: Seq[Int], names: Seq[String])
class FakeServicePrivateCtor private() /* private constructor */
class FakeServiceWithAnnotatedCtor(code: Int, name: String) {
  @Inject def this() = this(0, "")
}
