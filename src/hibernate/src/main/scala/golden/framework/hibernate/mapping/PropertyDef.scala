package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.{PropertyAccess, PropertyGenerated}

@JsonPropertyOrder(Array("column", "type"))
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
  val columns: Seq[ColumnDef] = Nil,
  val uniqueKey: Option[String] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "type")
  private def getType = propertyType.orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.orNull

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
  private def getGenerated = generated.orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumns = columns

  @JacksonXmlProperty(localName = "unique-key", isAttribute = true)
  private def getUniqueKey = uniqueKey.orNull

  // def applyUniqueKey(key: String): PropertyDef =
  //   if (!unique.contains(true))
  //     this
  //   else
  //     this.copy(uniqueKey = Some(key), unique = None, columns = this.columns.map(_.applyUniqueKey(key)))
