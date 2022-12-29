package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import golden.framework.hibernate.mapping.{CascadeStyle, ExtraLazyFetch, PropertyAccess}

@JacksonXmlRootElement(localName = "set")
class SetDef(
  name: String,
  key: KeyDef,
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
    collectionType)
