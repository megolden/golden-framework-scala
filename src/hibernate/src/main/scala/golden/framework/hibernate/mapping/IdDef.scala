package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  "name", " " + "type", " " + "column", "unsaved-value", "access",
  "column", "type", "generator"))
@JacksonXmlRootElement(localName = "id")
class IdDef(
  val name: String,
  val column: Option[ColumnDef] = None,
  val idType: Option[TypeDef] = None,
  val access: Option[PropertyAccess] = None,
  val generator: Option[IdGeneratorDef] = None,
  val unsavedValue: Option[String] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = " " + "column", isAttribute = true)
  private def getColumnName = column.collectFirst { case column if column.isNameOnly => column.name } .orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.filterNot(_.isNameOnly).orNull

  @JacksonXmlProperty(localName = " " + "type", isAttribute = true)
  private def getTypeName = idType.collectFirst { case tpe if !tpe.hasParameter => tpe.name } .orNull

  @JacksonXmlProperty(localName = "type")
  private def getType = idType.filter(_.hasParameter).orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.map(_.value).orNull

  @JacksonXmlProperty(localName = "generator")
  private def getGenerator = generator.orNull

  @JacksonXmlProperty(localName = "unsaved-value", isAttribute = true)
  private def getUnsavedValue = unsavedValue.orNull
