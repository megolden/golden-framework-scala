package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonPropertyOrder(Array(
  "name", "extends", "table", "discriminator-value", "mutable", "schema", 
  "dynamic-update", "dynamic-insert", "lazy", "abstract",
  "id", "composite-id", "key",
  "discriminator",
  "property", "component",
  "set", "bag", "list", "map",
  "one-to-one", "many-to-one",
  "subclass", "joined-subclass"))
abstract class ClassDefBase(
  val typeName: String,
  val isAbstract: Option[Boolean] = None,
  val lazyFetch: Option[Boolean] = None,
  val dynamicUpdate: Option[Boolean] = None,
  val dynamicInsert: Option[Boolean] = None,
  val properties: Iterable[PropertyDef] = Nil,
  val components: Iterable[ComponentDef] = Nil,
  val collections: Iterable[CollectionDef] = Nil,
  val oneToOnes: Iterable[OneToOneDef] = Nil,
  val manyToOnes: Iterable[ManyToOneDef] = Nil):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getTypeName = typeName

  @JacksonXmlProperty(localName = "abstract", isAttribute = true)
  private def getIsAbstract = isAbstract.orNull

  @JacksonXmlProperty(localName = "lazy", isAttribute = true)
  private def getLazyFetch = lazyFetch.orNull

  @JacksonXmlProperty(localName = "dynamic-update", isAttribute = true)
  private def getDynamicUpdate = dynamicUpdate.orNull

  @JacksonXmlProperty(localName = "dynamic-insert", isAttribute = true)
  private def getDynamicInsert = dynamicInsert.orNull

  @JacksonXmlProperty(localName = "property")
  private def getProperties = properties

  @JacksonXmlProperty(localName = "component")
  private def getComponents = components

  @JacksonXmlProperty(localName = "set")
  private def getSets = collections.collect { case set: SetDef => set }

  @JacksonXmlProperty(localName = "bag")
  private def getBags = collections.collect { case bag: BagDef => bag }

  @JacksonXmlProperty(localName = "list")
  private def getLists = collections.collect { case list: ListDef => list }

  @JacksonXmlProperty(localName = "map")
  private def getMaps = collections.collect { case map: MapDef => map }

  @JacksonXmlProperty(localName = "one-to-one")
  private def getOneToOnes = oneToOnes

  @JacksonXmlProperty(localName = "many-to-one")
  private def getManyToOnes = manyToOnes
