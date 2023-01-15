package golden.framework.bind

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import golden.framework.typeOf

class ContainerBuilderTests extends AnyFunSuite with Matchers:

  test("get should return service instance properly") {
    val container = aContainerWith {
      _.registerInstance(10)
    }

    val value = container.get[Int]

    value shouldBe 10
  }

  test("get should return service instance with specified type") {
    val container = aContainerWith {
      _.registerInstance[Number](10L)
    }

    val value = container.get[Number]

    value shouldBe 10L
    a [ServiceResolutionException] should be thrownBy {
      container.get[Long]
    }
  }

  test("get should return service instance by factory") {
    val container = aContainerWith { b =>
      b.registerInstance[Int](10)
      b.register[Long](_.get[Int] + 5)
    }

    val value = container.get[Long]

    value shouldBe 15L
  }

  test("get should return service instance by type") {
    val container = aContainerWith {
      _.registerType(typeOf[SomeService])
    }

    val instance = container.get[SomeService]

    instance shouldBe a [SomeService]
  }

  test("get should return annotated service instance properly") {
    val container = aContainerWith {
      _.registerService[SomeAnnotatedService]()
    }

    val iterable = container.get[Ordered[Int]]
    val instance = container.get[SomeAnnotatedService]

    iterable shouldBe a [SomeAnnotatedService]
    instance shouldBe a [SomeAnnotatedService]
  }

  test("get should throw exception when not annotated service type passed") {
    a [ServiceRegistrationException] should be thrownBy {
      aContainerWith { _.registerService[SomeNonAnnotatedService]() }
    }
  }

  test("registerModule should load a module properly") {
    val container = aContainerWith {
      _.registerModule(SomeIntModule)
    }

    noException should be thrownBy {
      container.get[Int]
    }
  }

  private def aContainerWith(setup: ContainerBuilder => ?): Container = {
    val builder = ContainerBuilder.create()
    setup(builder)
    builder.build()
  }

class SomeService

@service(typeOf[Ordered[Int]])
@service
class SomeAnnotatedService extends Ordered[Int] {
  override def compare(that: Int): Int = ???
}
class SomeNonAnnotatedService

object SomeIntModule extends Module {
  override def load(builder: ContainerBuilder): Unit = {
    builder.registerInstance(100)
  }
}
