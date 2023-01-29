package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  "name", " " + "type", " " + "column", "not-null", "length", "precision", "scale", "unique",
  "update", "insert", "generated", "unique-key", "access",
  "column", "type"))
@JacksonXmlRootElement(localName = "property")
class PropertyDef(
  val name: String,
  val propertyType: Option[TypeDef] = None,
  val access: Option[PropertyAccess] = None,
  val nullable: Option[Boolean] = None,
  val length: Option[Int] = None,
  val precision: Option[Int] = None,
  val scale: Option[Int] = None,
  val unique: Option[Boolean] = None,
  val insert: Option[Boolean] = None,
  val update: Option[Boolean] = None,
  val generated: Option[PropertyGenerated] = None,
  val columns: Iterable[ColumnDef] = Nil,
  val uniqueKey: Option[String] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = " " + "type", isAttribute = true)
  private def getTypeName = propertyType.collectFirst { case tpe if !tpe.hasParameter => tpe.name }.orNull

  @JacksonXmlProperty(localName = "type")
  private def getType = propertyType.filter(_.hasParameter).orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.map(_.value).orNull

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

  @JacksonXmlProperty(localName = "insert", isAttribute = true)
  private def getInsert = insert.orNull

  @JacksonXmlProperty(localName = "update", isAttribute = true)
  private def getUpdate = update.orNull

  @JacksonXmlProperty(localName = "generated", isAttribute = true)
  private def getGenerated = generated.map(_.value).orNull

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

  @JacksonXmlProperty(localName = "unique-key", isAttribute = true)
  private def getUniqueKey = uniqueKey.orNull
