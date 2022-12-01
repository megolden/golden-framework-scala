package golden.framework.domain

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ValueObjectTests extends AnyFunSuite with Matchers:

  test("equals and hashCode should works based on uniqueness values") {
    class Point(x: Int, y: Int) extends ValueObject:
      override protected def uniquenessValues: Seq[Any] = Seq(x, y)
    val point1 = new Point(1, 2)
    val point2 = new Point(1, 2)
    val diffPoint1 = new Point(2, 1)

    point1 should equal (point2)
    point1.hashCode should equal (point2.hashCode)
    diffPoint1 should not equal point1
    diffPoint1.hashCode should not equal point1.hashCode
  }
