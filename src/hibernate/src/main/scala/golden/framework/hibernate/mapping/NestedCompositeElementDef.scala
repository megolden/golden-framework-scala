package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  "name", "class", "access",
  "property", "nested-composite-element"))
@JacksonXmlRootElement(localName = "nested-composite-element")
class NestedCompositeElementDef(
  val name: String,
  val typeClass: String,
  val access: Option[PropertyAccess] = None,
  val properties: Iterable[PropertyDef] = Nil,
  val nestedComponentElements: Iterable[NestedCompositeElementDef] = Nil):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.map(_.value).orNull

  @JacksonXmlProperty(localName = "property")
  private def getProperties = properties

  @JacksonXmlProperty(localName = "nested-composite-element")
  private def getNestedComponentElements = nestedComponentElements
