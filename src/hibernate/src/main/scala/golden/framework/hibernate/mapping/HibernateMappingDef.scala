package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.{CascadeStyle, PropertyAccess}

@JsonPropertyOrder(Array(
  "schema", "catalog", "default-cascade", "default-access", "default-lazy",
  "typedef", "class"))
@JacksonXmlRootElement(localName = "hibernate-mapping")
class HibernateMappingDef(
  val schema: Option[String] = None,
  val catalog: Option[String] = None,
  val defaultCascade: Option[CascadeStyle] = None,
  val defaultAccess: Option[PropertyAccess] = None,
  val defaultLazy: Option[Boolean] = None,
  val userTypes: Iterable[UserTypeDef] = Nil,
  val classes: Iterable[ClassDef] = Nil):

  @JacksonXmlProperty(localName = "schema", isAttribute = true)
  private def getSchema = schema.orNull

  @JacksonXmlProperty(localName = "catalog", isAttribute = true)
  private def getCatalog = catalog.orNull

  @JacksonXmlProperty(localName = "default-cascade", isAttribute = true)
  private def getDefaultCascade = defaultCascade.map(_.value).orNull

  @JacksonXmlProperty(localName = "default-access", isAttribute = true)
  private def getDefaultAccess = defaultAccess.map(_.value).orNull

  @JacksonXmlProperty(localName = "default-lazy", isAttribute = true)
  private def getDefaultLazy = defaultLazy.orNull

  @JacksonXmlProperty(localName = "typedef")
  private def getUserTypes = userTypes

  @JacksonXmlProperty(localName = "class")
  private def getClasses = classes
