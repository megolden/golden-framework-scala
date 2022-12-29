package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.DeleteBehavior

@JacksonXmlRootElement(localName = "key")
class KeyDef(
  val nullable: Option[Boolean] = None,
  val column: Option[ColumnDef] = None,
  val propertyRef: Option[String] = None,
  val foreignKey: Option[String] = None,
  val unique: Option[Boolean] = None,
  val onDelete: Option[DeleteBehavior] = None):

  @JacksonXmlProperty(localName = "not-null", isAttribute = true)
  private def getNotNullable = nullable.map(!_).orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.orNull

  @JacksonXmlProperty(localName = "property-ref", isAttribute = true)
  private def getPropertyRef = propertyRef.orNull

  @JacksonXmlProperty(localName = "foreign-key", isAttribute = true)
  private def getForeignKey = foreignKey.orNull

  @JacksonXmlProperty(localName = "unique", isAttribute = true)
  private def getUnique = unique.orNull

  @JacksonXmlProperty(localName = "on-delete", isAttribute = true)
  private def getOnDelete = onDelete.orNull
