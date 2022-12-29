package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array("column", "type"))
@JacksonXmlRootElement(localName = "element")
class ElementDef(
  val elementType: Option[TypeDef] = None,
  val nullable: Option[Boolean] = None,
  val length: Option[Int] = None,
  val precision: Option[Int] = None,
  val scale: Option[Int] = None,
  val unique: Option[Boolean] = None,
  val columns: Seq[ColumnDef] = Nil):

  @JacksonXmlProperty(localName = "type")
  private def getType = elementType.orNull

  @JacksonXmlProperty(localName = "not-null", isAttribute = true)
  private def getNotNullable = nullable.map(!_).orNull

  @JacksonXmlProperty(localName = "length", isAttribute = true)
  private def getLength = length.orNull

  @JacksonXmlProperty(localName = "precision", isAttribute = true)
  private def getPrecision = precision.orNull

  @JacksonXmlProperty(localName = "scale", isAttribute = true)
  private def getScale = scale.orNull

  @JacksonXmlProperty(localName = "unique", isAttribute = true)
  private def getUnique = unique.orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumns = columns
