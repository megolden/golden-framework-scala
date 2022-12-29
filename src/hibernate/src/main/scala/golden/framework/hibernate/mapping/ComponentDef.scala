package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.PropertyAccess

@JsonPropertyOrder(Array("property", "many-to-one", "one-to-one", "component"))
@JacksonXmlRootElement(localName = "component")
class ComponentDef(
  val name: String,
  val typeClass: String,
  val access: Option[PropertyAccess] = None,
  val unique: Option[Boolean] = None,
  val lazyFetch: Option[Boolean] = None,
  val insert: Option[Boolean] = None,
  val update: Option[Boolean] = None,
  val properties: Iterable[PropertyDef] = Nil,
  val components: Iterable[ComponentDef] = Nil,
  val oneToOnes: Iterable[OneToOneDef] = Nil,
  val manyToOnes: Iterable[ManyToOneDef] = Nil):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.orNull

  @JacksonXmlProperty(localName = "unique", isAttribute = true)
  private def getUnique = unique.orNull

  @JacksonXmlProperty(localName = "lazy", isAttribute = true)
  private def getLazyFetch = lazyFetch.orNull

  @JacksonXmlProperty(localName = "insert", isAttribute = true)
  private def getInsert = insert.orNull

  @JacksonXmlProperty(localName = "update", isAttribute = true)
  private def getUpdate = update.orNull

  @JacksonXmlProperty(localName = "property")
  private def getProperties = properties

  @JacksonXmlProperty(localName = "component")
  private def getComponents = components

  @JacksonXmlProperty(localName = "one-to-one")
  private def getOneToOnes = oneToOnes

  @JacksonXmlProperty(localName = "many-to-one")
  private def getManyToOnes = manyToOnes
