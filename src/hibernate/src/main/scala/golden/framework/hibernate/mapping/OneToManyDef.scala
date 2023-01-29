package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array("class", "not-found"))
@JacksonXmlRootElement(localName = "one-to-many")
class OneToManyDef(val classType: String, val ignoreNotFound: Option[Boolean] = None):

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getClassType = classType

  @JacksonXmlProperty(localName = "not-found", isAttribute = true)
  private def getNotFound = ignoreNotFound match {
    case Some(true) => "ignore"
    case Some(false) => "exception"
    case _ => null
  }
