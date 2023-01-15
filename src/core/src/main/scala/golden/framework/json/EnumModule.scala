package golden.framework.json

import com.fasterxml.jackson.core.{JsonGenerator, JsonParser, JsonToken}
import com.fasterxml.jackson.databind.{BeanDescription,
  DeserializationConfig, DeserializationContext, JavaType,
  JsonDeserializer, KeyDeserializer, SerializerProvider}
import com.fasterxml.jackson.databind.SerializationFeature.{WRITE_ENUMS_USING_INDEX, WRITE_ENUM_KEYS_USING_INDEX}
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.module.{SimpleDeserializers, SimpleKeyDeserializers, SimpleModule}
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import scala.reflect.Enum
import golden.framework.ClassUtils.{findEnumFromOrdinalMethod, findEnumFromNameMethod}

class EnumModule extends SimpleModule:
  addSerializer(EnumSerializer())
  addKeySerializer(classOf[Enum], EnumKeySerializer())
  setDeserializers(EnumDeserializers())
  setKeyDeserializers(EnumKeyDeserializers())

class EnumSerializer extends StdScalarSerializer[Enum](classOf[Enum]):
  override def serialize(value: Enum, generator: JsonGenerator, provider: SerializerProvider): Unit =
    if (provider.isEnabled(WRITE_ENUMS_USING_INDEX))
      generator.writeNumber(value.ordinal)
    else
      generator.writeString(value.toString)

class EnumKeySerializer extends StdScalarSerializer[Enum](classOf[Enum]):
  override def serialize(value: Enum, generator: JsonGenerator, provider: SerializerProvider): Unit =
    if (provider.isEnabled(WRITE_ENUM_KEYS_USING_INDEX))
      generator.writeFieldName(value.ordinal.toString)
    else
      generator.writeFieldName(value.toString)

class EnumDeserializer[T <: Enum](enumType: Class[T]) extends StdScalarDeserializer[T](enumType):

  private val _fromOrdinal = findEnumFromOrdinalMethod[T](enumType)
  private val _fromName = findEnumFromNameMethod[T](enumType)

  override def deserialize(parser: JsonParser, context: DeserializationContext): T =
    if parser.hasToken(JsonToken.VALUE_NUMBER_INT) then _fromOrdinal(parser.getIntValue)
    else _fromName(parser.getValueAsString)

class EnumKeyDeserializer(enumType: Class[? <: Enum]) extends KeyDeserializer:

  private val _fromOrdinal = findEnumFromOrdinalMethod(enumType)
  private val _fromName = findEnumFromNameMethod(enumType)

  override def deserializeKey(key: String, context: DeserializationContext): AnyRef =
    val isOrdinal = Character.isDigit(key.charAt(0))
    if isOrdinal then _fromOrdinal(key.toInt).asInstanceOf[AnyRef]
    else _fromName(key).asInstanceOf[AnyRef]

class EnumDeserializers extends SimpleDeserializers:
  override def findBeanDeserializer(
    javaType: JavaType,
    config: DeserializationConfig,
    beanDesc: BeanDescription): JsonDeserializer[?] =

    if (classOf[Enum].isAssignableFrom(javaType.getRawClass))
      EnumDeserializer[Enum](javaType.getRawClass.asInstanceOf[Class[Enum]])
    else
      super.findBeanDeserializer(javaType, config, beanDesc)

class EnumKeyDeserializers extends SimpleKeyDeserializers:
  override def findKeyDeserializer(
    javaType: JavaType,
    config: DeserializationConfig,
    beanDesc: BeanDescription): KeyDeserializer =

    if (classOf[Enum].isAssignableFrom(javaType.getRawClass))
      EnumKeyDeserializer(javaType.getRawClass.asInstanceOf[Class[? <: Enum]])
    else
      super.findKeyDeserializer(javaType, config, beanDesc)
