package golden.framework.json

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import com.fasterxml.jackson.databind.{DeserializationContext, SerializerProvider}
import golden.framework.PInt

class PIntModule extends SimpleModule:
  addSerializer(PIntSerializer())
  addDeserializer(classOf[PInt], PIntDeserializer())

class PIntSerializer extends StdScalarSerializer[PInt](classOf[PInt]):
  override def serialize(value: PInt, generator: JsonGenerator, provider: SerializerProvider): Unit =
    generator.writeNumber(value.value)

class PIntDeserializer extends StdScalarDeserializer[PInt](classOf[PInt]):
  override def deserialize(parser: JsonParser, context: DeserializationContext): PInt =
    PInt.valueOf(parser.getIntValue)
