package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JacksonXmlRootElement(localName = "class")
class ClassDef(
  typeName: String,
  val id: IdDef | CompositeIdDef,
  isAbstract: Option[Boolean] = None,
  lazyFetch: Option[Boolean] = None,
  dynamicUpdate: Option[Boolean] = None,
  dynamicInsert: Option[Boolean] = None,
  val table: Option[String] = None,
  val schema: Option[String] = None,
  val mutable: Option[Boolean] = None,
  val discriminator: Option[DiscriminatorDef] = None,
  val discriminatorValue: Option[Any] = None,
  properties: Iterable[PropertyDef] = Nil,
  components: Iterable[ComponentDef] = Nil,
  collections: Iterable[CollectionDef] = Nil,
  oneToOnes: Iterable[OneToOneDef] = Nil,
  manyToOnes: Iterable[ManyToOneDef] = Nil,
  val subclasses: Iterable[SubclassDef] = Nil,
  val joinedSubclasses: Iterable[JoinedSubclassDef] = Nil)

  extends ClassDefBase(
    typeName,
    isAbstract,
    lazyFetch,
    dynamicUpdate,
    dynamicInsert,
    properties,
    components,
    collections,
    oneToOnes,
    manyToOnes):

  @JacksonXmlProperty(localName = "table", isAttribute = true)
  private def getTable = table.orNull

  @JacksonXmlProperty(localName = "schema", isAttribute = true)
  private def getSchema = schema.orNull

  @JacksonXmlProperty(localName = "mutable", isAttribute = true)
  private def getMutable = mutable.orNull

  @JacksonXmlProperty(localName = "id")
  private def getId = id match {
    case id: IdDef => id
    case _ => null
  }

  @JacksonXmlProperty(localName = "composite-id")
  private def getCompositeId = id match {
    case compositeId: CompositeIdDef => compositeId
    case _ => null
  }

  @JacksonXmlProperty(localName = "discriminator")
  private def getDiscriminator = discriminator.orNull

  @JacksonXmlProperty(localName = "discriminator-value", isAttribute = true)
  private def getDiscriminatorValue = discriminatorValue.orNull

  @JacksonXmlProperty(localName = "subclass")
  private def getSubclasses = subclasses

  @JacksonXmlProperty(localName = "joined-subclass")
  private def getJoinedSubclasses = joinedSubclasses
