package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  "name", " " + "type", " " + "column", "length", "access",
  "column", "type"))
@JacksonXmlRootElement(localName = "key-property")
class KeyPropertyDef(
  val name: String,
  val column: Option[ColumnDef] = None,
  val keyType: Option[TypeDef] = None,
  val access: Option[PropertyAccess] = None,
  val length: Option[Int] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = " " + "column", isAttribute = true)
  private def getColumnName = column.collectFirst { case column if column.isNameOnly => column.name }.orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.filterNot(_.isNameOnly).orNull

  @JacksonXmlProperty(localName = " " + "type", isAttribute = true)
  private def getTypeName = keyType.collectFirst { case tpe if !tpe.hasParameter => tpe.name }.orNull

  @JacksonXmlProperty(localName = "type")
  private def getType = keyType.filter(_.hasParameter).orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.map(_.value).orNull

  @JacksonXmlProperty(localName = "length", isAttribute = true)
  private def getLength = length.orNull
