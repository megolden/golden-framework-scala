package golden.framework.hibernate.mapping

enum PropertyAccess(name: String):
  case Field extends PropertyAccess("field")
  case Property extends PropertyAccess("property")

  override def toString: String = name
