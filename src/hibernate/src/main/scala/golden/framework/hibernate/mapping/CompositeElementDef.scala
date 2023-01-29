package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  "class",
  "property", "many-to-one", "nested-composite-element"))
@JacksonXmlRootElement(localName = "composite-element")
class CompositeElementDef(
  val typeClass: String,
  val properties: Iterable[PropertyDef] = Nil,
  val manyToOnes: Iterable[ManyToOneDef] = Nil,
  val nestedComponentElements: Iterable[NestedCompositeElementDef] = Nil):

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass

  @JacksonXmlProperty(localName = "property")
  private def getProperties = properties

  @JacksonXmlProperty(localName = "many-to-one")
  private def getManyToOnes = manyToOnes

  @JacksonXmlProperty(localName = "nested-composite-element")
  private def getNestedComponentElements = nestedComponentElements
