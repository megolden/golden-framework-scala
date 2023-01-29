package golden.framework.hibernate.mapping

enum PropertyAccess(val value: String):
  case Field extends PropertyAccess("field")
  case Property extends PropertyAccess("property")
  case Accessor(className: String) extends PropertyAccess(className)
