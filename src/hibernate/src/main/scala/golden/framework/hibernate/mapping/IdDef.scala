package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.PropertyAccess

@JsonPropertyOrder(Array("column", "type", "generator"))
@JacksonXmlRootElement(localName = "id")
class IdDef(
  val name: String,
  val column: Option[ColumnDef] = None,
  val idType: Option[TypeDef] = None,
  val access: Option[PropertyAccess] = None,
  val length: Option[Int] = None,
  val generator: Option[IdGeneratorDef] = None,
  val unsavedValue: Option[String] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.orNull

  @JacksonXmlProperty(localName = "type")
  private def getType = idType.orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.orNull

  @JacksonXmlProperty(localName = "length", isAttribute = true)
  private def getLength = length.orNull

  @JacksonXmlProperty(localName = "generator")
  private def getGenerator = generator.orNull

  @JacksonXmlProperty(localName = "unsaved-value", isAttribute = true)
  private def getUnsavedValue = unsavedValue.orNull
