package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.{CascadeStyle, ExtraLazyFetch, PropertyAccess}

@JacksonXmlRootElement(localName = "list")
class ListDef(
  name: String,
  key: KeyDef,
  val listIndex: ListIndexDef,
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

  @JacksonXmlProperty(localName = "list-index")
  private def getListIndex = listIndex

@JacksonXmlRootElement(localName = "list-index")
class ListIndexDef(val base: Option[Int] = None, val column: Option[ColumnDef] = None):

  @JacksonXmlProperty(localName = "base", isAttribute = true)
  private def getBase = base.orNull

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.orNull
