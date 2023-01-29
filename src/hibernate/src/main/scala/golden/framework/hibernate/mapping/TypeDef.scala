package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JacksonXmlRootElement(localName = "type")
class TypeDef(val name: String, val parameters: Iterable[ParameterDef] = Nil):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "param")
  private def getParameters = parameters

  def hasParameter: Boolean = parameters.nonEmpty
