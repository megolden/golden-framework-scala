package golden.framework.hibernate.mapping

enum PropertyGenerated(name: String):
  case Never extends PropertyGenerated("never")
  case Insert extends PropertyGenerated("insert")
  case Always extends PropertyGenerated("always")

  override def toString: String = name
