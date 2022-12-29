package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement, JacksonXmlText}

@JacksonXmlRootElement(localName = "param")
class ParameterDef(val name: String, val value: Any):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlText
  private def getValue = value
