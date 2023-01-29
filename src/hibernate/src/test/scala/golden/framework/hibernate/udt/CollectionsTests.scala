package golden.framework.hibernate.udt

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable
import golden.framework.hibernate.udt.*
import golden.framework.hibernate.udt.ImplicitConversions.*

class CollectionsTests extends AnyFunSuite with Matchers:

  test("collection types should be convert to equivalent scala collection types implicitly") {
    val set: Set[String] = mutable.HashSet.empty[String]
    val list: List[String] = mutable.ListBuffer.empty[String]
    val map: Map[String, Int] = mutable.HashMap.empty[String, Int]

    set.toSeq shouldBe empty
    list.toSeq shouldBe empty
    map.toSeq shouldBe empty
  }

  test("new item should be added using scala list operator to java list collection") {
    val javaList: List[String] = new mutable.ListBuffer[String]

    javaList += "some"

    javaList.size shouldBe 1
  }
