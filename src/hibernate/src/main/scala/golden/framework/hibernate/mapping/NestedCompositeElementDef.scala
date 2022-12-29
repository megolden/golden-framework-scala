package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.PropertyAccess

@JsonPropertyOrder(Array("property", "nested-composite-element"))
@JacksonXmlRootElement(localName = "nested-composite-element")
class NestedCompositeElementDef(
  val name: String,
  val typeClass: String,
  val access: Option[PropertyAccess] = None,
  val properties: Seq[PropertyDef] = Nil,
  val nestedComponentElements: Seq[NestedCompositeElementDef] = Nil):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.orNull

  @JacksonXmlProperty(localName = "property")
  private def getProperties = properties

  @JacksonXmlProperty(localName = "nested-composite-element")
  private def getNestedComponentElements = nestedComponentElements
