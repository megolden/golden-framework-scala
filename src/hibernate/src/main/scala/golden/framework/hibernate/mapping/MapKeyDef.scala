package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  " " + "column", " " + "type", "length",
  "column", "type"))
@JacksonXmlRootElement(localName = "map-key")
class MapKeyDef(val keyType: TypeDef, val length: Option[Int] = None, val column: Option[ColumnDef] = None):

  @JacksonXmlProperty(localName = " " + "type", isAttribute = true)
  private def getKeyTypeName = keyType.name

  @JacksonXmlProperty(localName = "type")
  private def getKeyType = keyType

  @JacksonXmlProperty(localName = "length", isAttribute = true)
  private def getLength = length.orNull

  @JacksonXmlProperty(localName = " " + "column", isAttribute = true)
  private def getColumnName = column.collectFirst { case column if column.isNameOnly => column.name }.orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.filterNot(_.isNameOnly).orNull
