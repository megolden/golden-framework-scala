package golden.framework.hibernate.udt

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable
import golden.framework.hibernate.udt
import udt.CollectionUtils.*

class CollectionUtilsTests extends AnyFunSuite with Matchers:

  test("collection types should be convert to equivalent scala collection types implicitly") {
    val set: udt.Set[String] = mutable.HashSet.empty[String]
    val list: udt.List[String] = mutable.ListBuffer.empty[String]
    val map: udt.Map[String, Int] = mutable.HashMap.empty[String, Int]

    set.toSeq shouldBe empty
    list.toSeq shouldBe empty
    map.toSeq shouldBe empty
  }

  test("new item should be added using scala list operator to java list collection") {
    val javaList: udt.List[String] = new mutable.ListBuffer[String]

    javaList += "some"

    javaList.size shouldBe 1
  }
