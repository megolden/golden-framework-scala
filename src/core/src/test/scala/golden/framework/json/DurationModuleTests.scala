package golden.framework.json

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import java.time.Duration

class DurationModuleTests extends AnyFunSuite with Matchers:

  test("Duration should serialize as nanos") {
    val serializer = DurationNumericSerializer()
    val result = serializeAsJson(
      Duration.parse("PT2H5M10S"),
      _.addSerializer(serializer))

    result should be ("7510000000000")
  }

  test("Duration should deserialize from json nanos") {
    val deserializer = DurationNumericDeserializer()
    val result = deserializeFromJson(
      "7510000000000", classOf[Duration],
      _.addDeserializer(classOf[Duration], deserializer))

    result should be (Duration.parse("PT2H5M10S"))
  }
