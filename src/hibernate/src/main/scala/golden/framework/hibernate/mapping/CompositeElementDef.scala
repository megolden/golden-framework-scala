package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array("property", "nested-composite-element"))
@JacksonXmlRootElement(localName = "composite-element")
class CompositeElementDef(
  val typeClass: String,
  val properties: Seq[PropertyDef] = Nil,
  val nestedComponentElements: Seq[NestedCompositeElementDef] = Nil):

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass

  @JacksonXmlProperty(localName = "property")
  private def getProperties = properties

  @JacksonXmlProperty(localName = "nested-composite-element")
  private def getNestedComponentElements = nestedComponentElements
