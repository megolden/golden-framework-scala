package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.PropertyAccess

@JsonPropertyOrder(Array("key-property", "generator"))
@JacksonXmlRootElement(localName = "composite-id")
class CompositeIdDef(
  val name: Option[String] = None,
  val typeClass: Option[String] = None,
  val access: Option[PropertyAccess] = None,
  val generator: Option[IdGeneratorDef] = None,
  val properties: Iterable[KeyPropertyDef] = Nil):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name.orNull

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass.orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.orNull

  @JacksonXmlProperty(localName = "generator")
  private def getGenerator = generator.orNull

  @JacksonXmlProperty(localName = "key-property")
  private def getProperties = properties