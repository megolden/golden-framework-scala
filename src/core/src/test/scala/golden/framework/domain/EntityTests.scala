package golden.framework.domain

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class EntityTests extends AnyFunSuite with Matchers:

  test("equals and hashCode should works based on id") {
    class DummyEntity(id: Long) extends Entity[Long](id)
    val entity1 = new DummyEntity(1)
    val entity2 = new DummyEntity(1)

    entity1 should equal (entity2)
    entity1.hashCode should equal (entity2.hashCode)
  }

  test("AutoIdEntity without ids should not be equals") {
    class DummyEntity extends AutoIdEntity[Long]
    val entityNoId1 = new DummyEntity
    val entityNoId1SameInstance = entityNoId1
    val entityNoId2 = new DummyEntity
    val entityWithId1 = new DummyEntity
    AutoIdEntity.setId(entityWithId1, 1)
    val entityWithId2 = new DummyEntity
    AutoIdEntity.setId(entityWithId2, 1)

    entityNoId1 should equal (entityNoId1SameInstance)
    entityNoId1 should not equal entityNoId2
    entityNoId1.hashCode should not equal entityNoId2.hashCode

    entityWithId1 should equal (entityWithId2)
    entityWithId1.hashCode should equal (entityWithId2.hashCode)
  }
