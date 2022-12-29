package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.{CascadeStyle, NoProxyLazyFetch, PropertyAccess}

@JacksonXmlRootElement(localName = "many-to-one")
class ManyToOneDef(
  val name: String,
  val typeClass: String,
  val column: Option[ColumnDef] = None,
  val foreignKey: Option[String] = None,
  val access: Option[PropertyAccess] = None,
  val lazyFetch: Option[NoProxyLazyFetch] = None,
  val cascade: Option[CascadeStyle] = None,
  val propertyRef: Option[String] = None,
  val unique: Option[Boolean] = None,
  val nullable: Option[Boolean] = None,
  val uniqueKey: Option[String] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.orNull

  @JacksonXmlProperty(localName = "foreign-key", isAttribute = true)
  private def getForeignKey = foreignKey.orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.orNull

  @JacksonXmlProperty(localName = "lazy", isAttribute = true)
  private def getLazyFetch = lazyFetch.orNull

  @JacksonXmlProperty(localName = "cascade", isAttribute = true)
  private def getCascade = cascade.orNull

  @JacksonXmlProperty(localName = "property-ref", isAttribute = true)
  private def getPropertyRef = propertyRef.orNull

  @JacksonXmlProperty(localName = "unique", isAttribute = true)
  private def getUnique = unique.orNull

  @JacksonXmlProperty(localName = "not-null", isAttribute = true)
  private def getNotNullable = nullable.map(!_).orNull

  @JacksonXmlProperty(localName = "unique-key", isAttribute = true)
  private def getUniqueKey = uniqueKey.orNull

  // def applyUniqueKey(key: String): ManyToOneDef =
  //   if (!unique.contains(true))
  //     this
  //   else
  //     this.copy(uniqueKey = Some(key), unique = None, column = this.column.map(_.applyUniqueKey(key)))
