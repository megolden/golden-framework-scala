package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  " " + "column", "base",
  "column"))
@JacksonXmlRootElement(localName = "list-index")
class ListIndexDef(val base: Option[Int] = None, val column: Option[ColumnDef] = None):

  @JacksonXmlProperty(localName = "base", isAttribute = true)
  private def getBase = base.orNull

  @JacksonXmlProperty(localName = " " + "column", isAttribute = true)
  private def getColumnName = column.collectFirst { case column if column.isNameOnly => column.name }.orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.filterNot(_.isNameOnly).orNull
