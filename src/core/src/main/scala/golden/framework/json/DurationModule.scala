package golden.framework.json

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import java.time.Duration

class DurationModule extends SimpleModule:
  addSerializer(DurationNumericSerializer())
  addDeserializer(classOf[Duration], DurationNumericDeserializer())

class DurationNumericSerializer extends StdScalarSerializer[Duration](classOf[Duration]):
  override def serialize(value: Duration, generator: JsonGenerator, provider: SerializerProvider): Unit =
    generator.writeNumber(value.toNanos)

class DurationNumericDeserializer extends StdScalarDeserializer[Duration](classOf[Duration]):
  override def deserialize(parser: JsonParser, context: DeserializationContext): Duration =
    Duration.ofNanos(parser.getLongValue)
