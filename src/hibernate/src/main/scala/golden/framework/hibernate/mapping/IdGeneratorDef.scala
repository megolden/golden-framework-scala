package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.IdGenerator

@JacksonXmlRootElement(localName = "generator")
class IdGeneratorDef(
  val classType: IdGenerator,
  val parameters: Seq[ParameterDef] = Nil):

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getClassType = classType

  @JacksonXmlProperty(localName = "param")
  private def getParameters = parameters
