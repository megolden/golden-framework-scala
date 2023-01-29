package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  " " + "column", " " + "type", "length", "precision", "scale", "not-null", "unique",
  "column", "type"))
@JacksonXmlRootElement(localName = "element")
class ElementDef(
  val elementType: Option[TypeDef] = None,
  val nullable: Option[Boolean] = None,
  val length: Option[Int] = None,
  val precision: Option[Int] = None,
  val scale: Option[Int] = None,
  val unique: Option[Boolean] = None,
  val columns: Iterable[ColumnDef] = Nil):

  @JacksonXmlProperty(localName = " " + "type", isAttribute = true)
  private def getTypeName = elementType.collectFirst { case tpe if !tpe.hasParameter => tpe.name }.orNull

  @JacksonXmlProperty(localName = "type")
  private def getType = elementType.filter(_.hasParameter).orNull

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

  @JacksonXmlProperty(localName = " " + "column", isAttribute = true)
  private def getColumnName = {
    if columns.size == 1 && columns.head.isNameOnly then columns.head.name
    else null
  }

  @JacksonXmlProperty(localName = "column")
  private def getColumns = {
    if columns.size == 1 && columns.head.isNameOnly then Nil
    else columns
  }
