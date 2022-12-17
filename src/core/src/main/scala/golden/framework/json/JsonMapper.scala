package golden.framework.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.core.`type`.TypeReference
import java.io.InputStream
import java.lang.reflect.Type

class JsonMapper:

  private val _mapper = {
    val mapper: ObjectMapper = new ObjectMapper()
      .enable(SerializationFeature.WRITE_ENUMS_USING_INDEX)
      .enable(SerializationFeature.WRITE_ENUM_KEYS_USING_INDEX)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
      .registerModule(Jdk8Module())
      .registerModule(JavaTimeModule())
      .registerModule(DefaultScalaModule)
      .registerModule(EnumModule())
    mapper
  }

  def serialize(value: Any): String =
    _mapper.writeValueAsString(value)

  def deserialize[T](json: String): T =
    _mapper.readValue[T](json, new TypeReference[T] {})

  def deserialize[T](json: String, tpe: Type): T =
    _mapper.readValue[T](json, _mapper.getTypeFactory.constructType(tpe))

  def deserialize[T](jsonStream: InputStream, tpe: Type): T =
    _mapper.readValue[T](jsonStream, _mapper.getTypeFactory.constructType(tpe))
