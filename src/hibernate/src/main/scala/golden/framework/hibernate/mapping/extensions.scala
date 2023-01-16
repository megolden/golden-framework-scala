package golden.framework.hibernate.mapping

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS
import com.fasterxml.jackson.databind.MapperFeature.{AUTO_DETECT_FIELDS, AUTO_DETECT_GETTERS, AUTO_DETECT_IS_GETTERS}
import scala.reflect.Enum

private[hibernate] def saveHibernateDefinitionAsXml(definition: Any): String =
  xmlMapper.writeValueAsString(definition)

private[hibernate] val xmlMapper = {
  XmlMapper.builder
    .defaultUseWrapper(false)
    .addModule(DefaultScalaModule)
    .addModule(new SimpleModule()
      .addSerializer(classOf[Enum],
        (value: Enum, generator: JsonGenerator, _: SerializerProvider) => generator.writeString(value.toString)))
    .serializationInclusion(NON_EMPTY)
    .disable(FAIL_ON_EMPTY_BEANS)
    .disable(AUTO_DETECT_FIELDS, AUTO_DETECT_GETTERS, AUTO_DETECT_IS_GETTERS)
    .build()
}
