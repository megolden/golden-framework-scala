package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array("name", "class"))
@JacksonXmlRootElement(localName = "typedef")
class UserTypeDef(val name: String, val typeClass: String, val parameters: Iterable[ParameterDef] = Nil):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass

  @JacksonXmlProperty(localName = "param")
  private def getParameters = parameters
