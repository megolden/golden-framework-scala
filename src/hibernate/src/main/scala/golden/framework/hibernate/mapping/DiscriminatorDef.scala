package golden.framework.hibernate.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlProperty, JacksonXmlRootElement}

@JacksonXmlRootElement(localName = "discriminator")
class DiscriminatorDef(
  val column: Option[ColumnDef] = None,
  val typeClass: Option[String] = None,
  val nullable: Option[Boolean] = None,
  val length: Option[Int] = None,
  val insert: Option[Boolean] = None,
  val force: Option[Boolean] = None):

  @JacksonXmlProperty(localName = "column")
  private def getColumn = column.orNull

  @JacksonXmlProperty(localName = "class", isAttribute = true)
  private def getTypeClass = typeClass.orNull

  @JacksonXmlProperty(localName = "not-null", isAttribute = true)
  private def getNotNullable = nullable.map(!_).orNull

  @JacksonXmlProperty(localName = "length", isAttribute = true)
  private def getLength = length.orNull

  @JacksonXmlProperty(localName = "insert", isAttribute = true)
  private def getInsert = insert.orNull

  @JacksonXmlProperty(localName = "force", isAttribute = true)
  private def getForce = force.orNull
