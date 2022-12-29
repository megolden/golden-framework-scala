package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.{CascadeStyle, ExtraLazyFetch, PropertyAccess}

@JacksonXmlRootElement(localName = "map")
class MapDef(
  name: String,
  key: KeyDef,
  val mapKey: MapKeyDef,
  table: Option[String] = None,
  lazyFetch: Option[ExtraLazyFetch] = None,
  cascade: Option[CascadeStyle] = None,
  access: Option[PropertyAccess] = None,
  inverse: Option[Boolean] = None,
  oneToMany: Option[OneToManyDef] = None,
  manyToMany: Option[ManyToManyDef] = None,
  element: Option[ElementDef] = None,
  compositeElement: Option[CompositeElementDef] = None,
  mutable: Option[Boolean] = None,
  collectionType: Option[String] = None)
  extends CollectionDef(
    name,
    key,
    table,
    lazyFetch,
    cascade,
    access,
    inverse,
    oneToMany,
    manyToMany,
    element,
    compositeElement,
    mutable,
    collectionType):

  @JacksonXmlProperty(localName = "map-key")
  private def getMapKey = mapKey

@JsonPropertyOrder(Array("column", "type"))
@JacksonXmlRootElement(localName = "map-key")
class MapKeyDef(val keyType: TypeDef, val length: Option[Int] = None, val column: Option[ColumnDef] = None):

  @JacksonXmlProperty(localName = " " + "type", isAttribute = true)
  private def getKeyTypeName = keyType.name

  @JacksonXmlProperty(localName = "type")
  private def getKeyType = keyType

  @JacksonXmlProperty(localName = "length", isAttribute = true)
  private def getLength = length.orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.orNull
