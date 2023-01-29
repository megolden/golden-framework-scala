package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(" " + "column", "type", "insert"))
@JacksonXmlRootElement(localName = "discriminator")
class DiscriminatorDef(
  val column: Option[ColumnDef] = None,
  val typeName: Option[String] = None,
  val insert: Option[Boolean] = None):

  @JacksonXmlProperty(localName = " " + "column", isAttribute = true)
  private def getColumnName = column.collectFirst { case column if column.isNameOnly => column.name } .orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.filterNot(_.isNameOnly).orNull

  @JacksonXmlProperty(localName = "type", isAttribute = true)
  private def getTypeName = typeName.orNull

  @JacksonXmlProperty(localName = "insert", isAttribute = true)
  private def getInsert = insert.orNull
