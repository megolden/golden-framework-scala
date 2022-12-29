package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JacksonXmlRootElement(localName = "one-to-many")
class OneToManyDef(val classType: String):

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getClassType = classType
