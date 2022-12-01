package golden.framework.json

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, LocalTime, ZoneOffset}

class DateTimeModule extends SimpleModule:
  addSerializer(LocalTimeToNanoSerializer())
  addDeserializer(classOf[LocalTime], LocalTimeToNanoDeserializer())
  addSerializer(LocalDateTimeUTCSerializer())

class LocalTimeToNanoSerializer extends StdScalarSerializer[LocalTime](classOf[LocalTime]):
  override def serialize(value: LocalTime, generator: JsonGenerator, provider: SerializerProvider): Unit =
    generator.writeNumber(value.toNanoOfDay)

class LocalTimeToNanoDeserializer extends StdScalarDeserializer[LocalTime](classOf[LocalTime]):
  override def deserialize(parser: JsonParser, context: DeserializationContext): LocalTime =
    LocalTime.ofNanoOfDay(parser.getLongValue)

class LocalDateTimeUTCSerializer extends StdScalarSerializer[LocalDateTime](classOf[LocalDateTime]):
  override def serialize(value: LocalDateTime, generator: JsonGenerator, provider: SerializerProvider): Unit =
    generator.writeString(value.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
