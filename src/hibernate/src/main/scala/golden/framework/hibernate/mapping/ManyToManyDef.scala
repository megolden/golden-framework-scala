package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.LazyFetch

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

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.orNull

  @JacksonXmlProperty(localName = "foreign-key", isAttribute = true)
  private def getForeignKey = foreignKey.orNull

  @JacksonXmlProperty(localName = "lazy", isAttribute = true)
  private def getLazyFetch = lazyFetch.orNull

  @JacksonXmlProperty(localName = "property-ref", isAttribute = true)
  private def getPropertyRef = propertyRef.orNull

  @JacksonXmlProperty(localName = "unique", isAttribute = true)
  private def getUnique = unique.orNull
