package golden.framework.hibernate

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS
import com.fasterxml.jackson.databind.MapperFeature.{AUTO_DETECT_FIELDS, AUTO_DETECT_GETTERS, AUTO_DETECT_IS_GETTERS}

package object mapping:

  private[hibernate] def generateHbmXml(definition: Any): String =
    xmlMapper.writeValueAsString(definition)

  private[hibernate] val xmlMapper = {
    XmlMapper.builder
      .defaultUseWrapper(false)
      .addModule(DefaultScalaModule)
      .serializationInclusion(NON_EMPTY)
      .disable(FAIL_ON_EMPTY_BEANS)
      .disable(AUTO_DETECT_FIELDS, AUTO_DETECT_GETTERS, AUTO_DETECT_IS_GETTERS)
      .build()
  }
