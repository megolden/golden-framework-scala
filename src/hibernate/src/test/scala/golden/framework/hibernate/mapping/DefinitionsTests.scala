package golden.framework.hibernate.mapping

import golden.framework.hibernate.XmlSuite
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class DefinitionsTests extends AnyFunSuite with Matchers with XmlSuite:

  test("CascadeStyle should store in xml format properly") {
    CascadeStyle.All.value should be ("all")
    CascadeStyle.AllDeleteOrphan.value should be ("all-delete-orphan")
    CascadeStyle.DeleteOrphan.value should be ("delete-orphan")
    CascadeStyle.Create.value should be ("create")
    CascadeStyle.Evict.value should be ("evict")
    CascadeStyle.Lock.value should be ("lock")
    CascadeStyle.Merge.value should be ("merge")
    CascadeStyle.Persist.value should be ("persist")
    CascadeStyle.Refresh.value should be ("refresh")
    CascadeStyle.Duplicate.value should be ("duplicate")
    CascadeStyle.Delete.value should be ("delete")
    CascadeStyle.None.value should be ("none")
    CascadeStyle.SaveUpdate.value should be ("save-update")
  }

  test("PropertyAccess should store in xml format properly") {
    PropertyAccess.Field.value should be ("field")
    PropertyAccess.Property.value should be ("property")
    PropertyAccess.Accessor("some").value should be ("some")
  }

  test("PropertyGenerated should store in xml format properly") {
    PropertyGenerated.Never.value should be ("never")
    PropertyGenerated.Insert.value should be ("insert")
    PropertyGenerated.Always.value should be ("always")
  }

  test("NoProxyLazyFetch should store in xml format properly") {
    NoProxyLazyFetch.False.value should be ("false")
    NoProxyLazyFetch.Proxy.value should be ("proxy")
    NoProxyLazyFetch.NoProxy.value should be ("no-proxy")
  }

  test("LazyFetch should store in xml format properly") {
    LazyFetch.False.value should be ("false")
    LazyFetch.Proxy.value should be ("proxy")
  }

  test("IdGenerator should store in xml format properly") {
    IdGenerator.Increment.value should be ("increment")
    IdGenerator.Identity.value should be ("identity")
    IdGenerator.Foreign.value should be ("foreign")
    IdGenerator.UUID.value should be ("uuid")
    IdGenerator.GUID.value should be ("guid")
    IdGenerator.Assigned.value should be ("assigned")
  }

  test("DeleteBehavior should store in xml format properly") {
    DeleteBehavior.NoAction.value should be ("noaction")
    DeleteBehavior.Cascade.value should be ("cascade")
  }

  test("ExtraLazyFetch should store in xml format properly") {
    ExtraLazyFetch.True.value should be ("true")
    ExtraLazyFetch.False.value should be ("false")
    ExtraLazyFetch.Extra.value should be ("extra")
  }

  test("HibernateMappingDef should store in xml format properly") {
    val definition = new HibernateMappingDef(
      schema = Some("some_schema"),
      catalog = Some("some_catalog"),
      defaultCascade = Some(CascadeStyle.Delete),
      defaultAccess = Some(PropertyAccess.Property),
      defaultLazy = Some(true),
      userTypes = Seq(new UserTypeDef("some_name", "some_type_class")),
      classes = Seq(new ClassDef("some_class", new IdDef("some_id"))))

    val element = generateHbmXml(definition).asXmlElement

    element.getName should be ("hibernate-mapping")
    element.attributeCount should be (5)

    element.attribute(0).getName should be ("schema")
    element.attribute(0).getValue should be ("some_schema")

    element.attribute(1).getName should be ("catalog")
    element.attribute(1).getValue should be ("some_catalog")

    element.attribute(2).getName should be ("default-cascade")
    element.attribute(2).getValue should be (CascadeStyle.Delete.value)

    element.attribute(3).getName should be ("default-access")
    element.attribute(3).getValue should be (PropertyAccess.Property.value)

    element.attribute(4).getName should be ("default-lazy")
    element.attribute(4).getValue should be ("true")

    element.elements.size should be (2)
    element.element(0).getName should be ("typedef")
    element.element(1).getName should be ("class")
  }

  test("PropertyDef with simple type should store in xml format properly") {
    val definition = new PropertyDef(
      name = "some_name",
      propertyType = Some(TypeDef("string")))

    val element = generateHbmXml(definition).asXmlElement

    element.getName should be ("property")
    element.attributeCount should be (2)

    element.attribute(0).getName should be ("name")
    element.attribute(0).getValue should be ("some_name")

    element.attribute(1).getName should be ("type")
    element.attribute(1).getValue should be ("string")

    element.elements should be (empty)
  }

  test("PropertyDef with complex type should store in xml format properly") {
    val definition = new PropertyDef(
      name = "some_name",
      propertyType = Some(TypeDef("Int", Seq(new ParameterDef("unsigned", true)))))

    val element = generateHbmXml(definition).asXmlElement

    element.attributeCount should be (1)

    element.attribute(0).getName should be ("name")
    element.attribute(0).getValue should be ("some_name")

    element.elements.size should be (1)
    element.element(0).getName should be ("type")
  }
