package golden.framework.hibernate.mapping

enum PropertyAccess(name: String):
  case Field extends PropertyAccess("field")
  case Property extends PropertyAccess("property")
  case Accessor(className: String) extends PropertyAccess(className)

  override def toString: String = name
