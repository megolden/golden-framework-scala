package golden.framework.bind

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.mockito.Mockito.*
import java.io.Closeable
import scala.collection.mutable

class ContainerTests extends AnyFunSuite with Matchers:

  test("get should return lazy service instance properly") {
    val container = aContainerWith {
      _.register[Int](_ => throw Exception())
    }

    noException should be thrownBy {
      container.get[Lazy[Int]]
    }
    val lazyService = container.get[Lazy[Int]]
    an [Exception] should be thrownBy {
      lazyService.get
    }
  }

  test("get should return all instances of a service") {
    val container = aContainerWith { b =>
      b.registerInstance[Int](1)
      b.registerInstance[Int](2)
      b.registerInstance[Int](3)
    }

    val values = container.get[List[Int]]

    values shouldBe List(1, 2, 3)
  }

  test("get should return last instance of a service") {
    val container = aContainerWith { b =>
      b.registerInstance[Int](1)
      b.registerInstance[Int](2)
    }

    val value = container.get[Int]

    value shouldBe 2
  }

  test("get should return optional instance of a service") {
    val container = aContainerWith { b =>
      b.registerInstance[Int](10)
    }

    val registeredValue = container.get[Option[Int]]
    val notRegisteredValue = container.get[Option[Long]]

    registeredValue shouldBe Some(10)
    notRegisteredValue shouldBe None
  }

  test("get should throw exception when not registered service type passed") {
    val container = aContainer()

    a [ServiceResolutionException] should be thrownBy {
      container.get[Long]
    }
  }

  test("isRoot should return container is root or no") {
    val rootContainer = aContainer()
    val childContainer = rootContainer.createScope()
    val childOfChildContainer = childContainer.createScope()

    rootContainer.isRoot shouldBe true
    childContainer.isRoot shouldBe false
    childOfChildContainer.isRoot shouldBe false
  }

  test("root should return root container") {
    val rootContainer = aContainer()
    val childContainer = rootContainer.createScope()
    val childOfChildContainer = childContainer.createScope()

    rootContainer.root should be theSameInstanceAs rootContainer
    childContainer.root should be theSameInstanceAs rootContainer
    childOfChildContainer.root should be theSameInstanceAs rootContainer
  }

  test("child container with addition services should be instantiate") {
    val container = aContainer()
    val childContainer = container.createScope(_.registerInstance[Int](10))

    a [ServiceResolutionException] should be thrownBy {
      container.get[Int]
    }
    noException should be thrownBy {
      childContainer.get[Int]
    }
    childContainer.get[Int] shouldBe 10
  }

  test("containers should be created with tags") {
    val rootContainer = aContainerWithTags("parent")
    val childContainer = rootContainer.createScope("child")

    rootContainer.tags should have size 2
    rootContainer.tags should contain ("root")
    rootContainer.tags should contain ("parent")
    childContainer.tags shouldBe Set("child")
  }

  test("transient service should instantiate properly") {
    val container = aContainerWith {
      _.registerType[SomeTransientService]
    }

    val instance1 = container.get[SomeTransientService]
    val instance2 = container.get[SomeTransientService]

    instance2 should not be theSameInstanceAs (instance1)
  }

  test("scoped service should instantiate properly") {
    val container1 = aContainerWith {
      _.registerType[SomeTransientService].asContainerScoped()
    }
    val container2 = container1.createScope()

    val scope1Instance1 = container1.get[SomeTransientService]
    val scope1Instance2 = container1.get[SomeTransientService]
    val scope2Instance = container2.get[SomeTransientService]

    scope1Instance1 should be theSameInstanceAs scope1Instance2
    scope2Instance should not be theSameInstanceAs (scope1Instance1)
    scope2Instance should not be theSameInstanceAs (scope1Instance2)
  }

  test("singleton service should instantiate properly") {
    val container1 = aContainerWith {
      _.registerType[SomeTransientService].asSingleton()
    }
    val container2 = container1.createScope()

    val scope1Instance1 = container1.get[SomeTransientService]
    val scope1Instance2 = container1.get[SomeTransientService]
    val scope2Instance = container2.get[SomeTransientService]

    scope1Instance1 should be theSameInstanceAs scope1Instance2
    scope2Instance should be theSameInstanceAs scope1Instance1
  }

  test("non singleton service instances should be closed at end of its scope lifetime") {
    val container1 = aContainerWith {
      _.register[SomeCloseableService](_ => mock(classOf[SomeCloseableService]))
    }
    val container2 = container1.createScope()
    val container1Instance = container1.get[SomeCloseableService]
    val container2Instance = container2.get[SomeCloseableService]
    container2.close()

    verify(container1Instance, never()).close()
    verify(container2Instance, times(1)).close()
  }

  test("singleton service instances should be closed only at end of root container lifetime") {
    val rootContainer = aContainerWith {
      _.register[SomeCloseableService](_ => mock(classOf[SomeCloseableService])).asSingleton()
    }
    val childContainer = rootContainer.createScope()
    val rootInstance = rootContainer.get[SomeCloseableService]
    val childInstance = childContainer.get[SomeCloseableService]
    childContainer.close()

    verify(rootInstance, never()).close()
    verify(childInstance, never()).close()
    rootContainer.close()
    verify(rootInstance, times(1)).close()
    verify(childInstance, times(1)).close()
  }

  test("closeable service instances should be closed with reverse resolution order") {
    val closedEventLogger = new ClosedEventLogger
    val container = aContainerWith { b =>
      b.registerInstance(closedEventLogger)
      b.registerType[SomeCloseableService]
      b.registerType[SomeDependentCloseableService]
    }

    container.get[SomeDependentCloseableService]
    container.close()

    closedEventLogger.closedObjects should have size 2
    closedEventLogger.closedObjects(0) shouldBe a[SomeCloseableService]
    closedEventLogger.closedObjects(1) shouldBe a[SomeDependentCloseableService]
  }

  test("externally owned closeable service instances should not be closed by containers") {
    val container = aContainerWith {
      _.registerInstance[SomeCloseableService](mock(classOf[SomeCloseableService])).externallyOwned()
    }

    val instance = container.get[SomeCloseableService]
    container.close()

    verify(instance, never()).close()
  }

  private def aContainerWith(setup: ContainerBuilder => ?, tags: Any*): Container = {
    val builder = ContainerBuilder.create()
    Option(setup).tapEach(_.apply(builder))
    builder.build(tags*)
  }
  private def aContainer(): Container =
    aContainerWith(setup = null)
  private def aContainerWithTags(tags: Any*): Container =
    aContainerWith(setup = null, tags*)

class SomeTransientService
class SomeLazyService
class ClosedEventLogger {
  private val closedSequence = mutable.ArrayBuffer.empty[Any]
  def closedObjects: List[Any] = closedSequence.toList
  def closed(obj: Any): Unit = closedSequence += obj
}
class SomeCloseableService(closedEventLogger: ClosedEventLogger) extends Closeable {
  override def close(): Unit = closedEventLogger.closed(this)
}
class SomeDependentCloseableService(closeable: SomeCloseableService, closedEventLogger: ClosedEventLogger) extends Closeable {
  override def close(): Unit = closedEventLogger.closed(this)
}
