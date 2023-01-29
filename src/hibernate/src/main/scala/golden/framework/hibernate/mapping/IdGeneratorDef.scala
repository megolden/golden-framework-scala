package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JacksonXmlRootElement(localName = "generator")
class IdGeneratorDef(
  val classType: IdGenerator,
  val parameters: Iterable[ParameterDef] = Nil):

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getClassType = classType.value

  @JacksonXmlProperty(localName = "param")
  private def getParameters = parameters
