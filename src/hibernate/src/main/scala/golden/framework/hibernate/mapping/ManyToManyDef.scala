package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JsonPropertyOrder(Array(
  "class", " " + "column", "lazy", "foreign-key", "property-ref", "unique",
  "column"))
@JacksonXmlRootElement(localName = "many-to-many")
class ManyToManyDef(
  val typeClass: String,
  val column: Option[ColumnDef] = None,
  val foreignKey: Option[String] = None,
  val lazyFetch: Option[LazyFetch] = None,
  val propertyRef: Option[String] = None,
  val unique: Option[Boolean] = None):

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass

  @JacksonXmlProperty(localName = " " + "column", isAttribute = true)
  private def getColumnName = column.collectFirst { case column if column.isNameOnly => column.name }.orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.filterNot(_.isNameOnly).orNull

  @JacksonXmlProperty(localName = "foreign-key", isAttribute = true)
  private def getForeignKey = foreignKey.orNull

  @JacksonXmlProperty(localName = "lazy", isAttribute = true)
  private def getLazyFetch = lazyFetch.map(_.value).orNull

  @JacksonXmlProperty(localName = "property-ref", isAttribute = true)
  private def getPropertyRef = propertyRef.orNull

  @JacksonXmlProperty(localName = "unique", isAttribute = true)
  private def getUnique = unique.orNull
