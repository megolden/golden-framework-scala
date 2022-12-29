package golden.framework.hibernate.mapping

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}
import golden.framework.hibernate.mapping.PropertyAccess

@JsonPropertyOrder(Array("column", "type"))
@JacksonXmlRootElement(localName = "key-property")
class KeyPropertyDef(
  val name: String,
  val column: Option[ColumnDef] = None,
  val keyType: Option[TypeDef] = None,
  val access: Option[PropertyAccess] = None,
  val length: Option[Int] = None):

  @JacksonXmlProperty(localName = "name", isAttribute = true)
  private def getName = name

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.orNull

  @JacksonXmlProperty(localName = "type")
  private def getType = keyType.orNull

  @JacksonXmlProperty(localName = "access", isAttribute = true)
  private def getAccess = access.orNull

  @JacksonXmlProperty(localName = "length", isAttribute = true)
  private def getLength = length.orNull
