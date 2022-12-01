package golden.framework.json

import golden.framework.PInt
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PIntModuleTests extends AnyFunSuite with Matchers:

  test("PInt should serialize as integer") {
    val serializer = PIntSerializer()
    val result = serializeAsJson(
      PInt(150),
      _.addSerializer(serializer))

    result should be ("150")
  }

  test("PInt should deserialize from json integer") {
    val deserializer = PIntDeserializer()
    val result = deserializeFromJson(
      "150", classOf[PInt],
      _.addDeserializer(classOf[PInt], deserializer))

    result should be (PInt(150))
  }
