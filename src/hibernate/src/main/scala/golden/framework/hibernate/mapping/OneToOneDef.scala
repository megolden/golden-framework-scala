package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  "name", "class", "cascade", "constrained", "property-ref", "access", "lazy", "foreign-key",
  "column", "type"))
@JacksonXmlRootElement(localName = "one-to-one")
class OneToOneDef(
  val name: String,
  val typeClass: String,
  val foreignKey: Option[String] = None,
  val access: Option[PropertyAccess] = None,
  val lazyFetch: Option[NoProxyLazyFetch] = None,
  val cascade: Option[CascadeStyle] = None,
  val propertyRef: Option[String] = None,
  val constrained: Option[Boolean] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass

  @JacksonXmlProperty(localName = "foreign-key", isAttribute = true)
  private def getForeignKey = foreignKey.orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.map(_.value).orNull

  @JacksonXmlProperty(localName = "lazy", isAttribute = true)
  private def getLazyFetch = lazyFetch.map(_.value).orNull

  @JacksonXmlProperty(localName = "cascade", isAttribute = true)
  private def getCascade = cascade.map(_.value).orNull

  @JacksonXmlProperty(localName = "property-ref", isAttribute = true)
  private def getPropertyRef = propertyRef.orNull

  @JacksonXmlProperty(localName = "constrained", isAttribute = true)
  private def getConstrained = constrained.orNull
