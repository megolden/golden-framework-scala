package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import golden.framework.hibernate.mapping.{CascadeStyle, ExtraLazyFetch, PropertyAccess}

@JsonPropertyOrder(Array(
  "key", "list-index", "map-key", "element",
  "composite-element", "one-to-many", "many-to-many"))
abstract class CollectionDef(
  val name: String,
  val key: KeyDef,
  val table: Option[String] = None,
  val lazyFetch: Option[ExtraLazyFetch] = None,
  val cascade: Option[CascadeStyle] = None,
  val access: Option[PropertyAccess] = None,
  val inverse: Option[Boolean] = None,
  val oneToMany: Option[OneToManyDef] = None,
  val manyToMany: Option[ManyToManyDef] = None,
  val element: Option[ElementDef] = None,
  val compositeElement: Option[CompositeElementDef] = None,
  val mutable: Option[Boolean] = None,
  val collectionType: Option[String] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "key")
  private def getKey = key

  @JacksonXmlProperty(localName = "table", isAttribute = true)
  private def getTable = table.orNull

  @JacksonXmlProperty(localName = "lazy", isAttribute = true)
  private def getLazyFetch = lazyFetch.orNull

  @JacksonXmlProperty(localName = "cascade", isAttribute = true)
  private def getCascade = cascade.orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.orNull

  @JacksonXmlProperty(localName = "inverse", isAttribute = true)
  private def getInverse = inverse.orNull

  @JacksonXmlProperty(localName = "one-to-many")
  private def getOneToMany = oneToMany.orNull

  @JacksonXmlProperty(localName = "many-to-many")
  private def getManyToMany = manyToMany.orNull

  @JacksonXmlProperty(localName = "element")
  private def getElement = element.orNull

  @JacksonXmlProperty(localName = "composite-element")
  private def getCompositeElement = compositeElement.orNull

  @JacksonXmlProperty(localName = "mutable", isAttribute = true)
  private def getMutable = mutable.orNull

  @JacksonXmlProperty(localName = "collection-type", isAttribute = true)
  private def getCollectionType = collectionType.orNull
