package golden.framework.json

import golden.framework.json.LocalDateTimeUTCSerializer
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import java.time.{LocalDateTime, LocalTime}

class DateTimeModuleTests extends AnyFunSuite with Matchers:

  test("LocalDateTime should be serialize in UTC format") {
    val serializer = LocalDateTimeUTCSerializer()

    val result = serializeAsJson(
      LocalDateTime.of(2022, 7, 12, 8, 10, 30),
      _.addSerializer(serializer))

    result should be ("\"2022-07-12T08:10:30Z\"")
  }

  test("LocalTime should be serialize as nanos") {
    val serializer = LocalTimeToNanoSerializer()
    val result = serializeAsJson(
      LocalTime.of(8, 10, 30, 500),
      _.addSerializer(serializer))

    result should be ("29430000000500")
  }

  test("LocalTime should be deserialize from json nanos") {
    val deserializer = LocalTimeToNanoDeserializer()
    val result = deserializeFromJson(
      "29430000000500", classOf[LocalTime],
      _.addDeserializer(classOf[LocalTime], deserializer))

    result should be (LocalTime.of(8, 10, 30, 500))
  }
