package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  "name", "length", "precision", "scale", "not-null", "unique", "unique-key", "sql-type", "default"))
@JacksonXmlRootElement(localName = "column")
class ColumnDef(
  val name: String,
  val length: Option[Int] = None,
  val nullable: Option[Boolean] = None,
  val sqlType: Option[String] = None,
  val precision: Option[Int] = None,
  val scale: Option[Int] = None,
  val unique: Option[Boolean] = None,
  val defaultValue: Option[String] = None,
  val uniqueKey: Option[String] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "length", isAttribute = true)
  private def getLength = length.orNull

  @JacksonXmlProperty(localName = "not-null", isAttribute = true)
  private def getNotNullable = nullable.map(!_).orNull

  @JacksonXmlProperty(localName = "sql-type", isAttribute = true)
  private def getSqlType = sqlType.orNull

  @JacksonXmlProperty(localName = "precision", isAttribute = true)
  private def getPrecision = precision.orNull

  @JacksonXmlProperty(localName = "scale", isAttribute = true)
  private def getScale = scale.orNull

  @JacksonXmlProperty(localName = "unique", isAttribute = true)
  private def getUnique = unique.orNull

  @JacksonXmlProperty(localName = "default", isAttribute = true)
  private def getDefaultValue = defaultValue.orNull

  @JacksonXmlProperty(localName = "unique-key", isAttribute = true)
  private def getUniqueKey = uniqueKey.orNull

  def isNameOnly: Boolean =
    length.isEmpty &&
    nullable.isEmpty &&
    sqlType.isEmpty &&
    precision.isEmpty &&
    scale.isEmpty &&
    unique.isEmpty &&
    defaultValue.isEmpty &&
    uniqueKey.isEmpty
