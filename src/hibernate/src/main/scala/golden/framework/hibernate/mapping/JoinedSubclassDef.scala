package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JacksonXmlRootElement(localName = "joined-subclass")
class JoinedSubclassDef(
  typeName: String,
  val key: KeyDef,
  val extendedType: Option[String] = None,
  isAbstract: Option[Boolean] = None,
  lazyFetch: Option[Boolean] = None,
  dynamicUpdate: Option[Boolean] = None,
  dynamicInsert: Option[Boolean] = None,
  val table: Option[String] = None,
  val schema: Option[String] = None,
  properties: Seq[PropertyDef] = Nil,
  components: Iterable[ComponentDef] = Nil,
  collections: Iterable[CollectionDef] = Nil,
  oneToOnes: Iterable[OneToOneDef] = Nil,
  manyToOnes: Iterable[ManyToOneDef] = Nil,
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

  @JacksonXmlProperty(localName = "key")
  private def getKey = key

  @JacksonXmlProperty(localName = "extends", isAttribute = true)
  private def getExtendedType = extendedType.orNull

  @JacksonXmlProperty(localName = "table", isAttribute = true)
  private def getTable = table.orNull

  @JacksonXmlProperty(localName = "schema", isAttribute = true)
  private def getSchema = schema.orNull

  @JacksonXmlProperty(localName = "joined-subclass")
  private def getJoinedSubclasses = joinedSubclasses
