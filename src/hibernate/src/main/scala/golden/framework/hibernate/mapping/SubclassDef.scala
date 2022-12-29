package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JacksonXmlRootElement(localName = "subclass")
class SubclassDef(
  typeName: String,
  val extendedType: Option[String] = None,
  val discriminatorValue: Option[Any] = None,
  isAbstract: Option[Boolean] = None,
  lazyFetch: Option[Boolean] = None,
  dynamicUpdate: Option[Boolean] = None,
  dynamicInsert: Option[Boolean] = None,
  properties: Seq[PropertyDef] = Nil,
  components: Iterable[ComponentDef] = Nil,
  collections: Iterable[CollectionDef] = Nil,
  oneToOnes: Iterable[OneToOneDef] = Nil,
  manyToOnes: Iterable[ManyToOneDef] = Nil,
  val subclasses: Iterable[SubclassDef] = Nil)

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

  @JacksonXmlProperty(localName = "extends", isAttribute = true)
  private def getExtendedType = extendedType.orNull

  @JacksonXmlProperty(localName = "discriminator-value", isAttribute = true)
  private def getDiscriminatorValue = discriminatorValue.orNull

  @JacksonXmlProperty(localName = "subclass")
  private def getSubclasses = subclasses
