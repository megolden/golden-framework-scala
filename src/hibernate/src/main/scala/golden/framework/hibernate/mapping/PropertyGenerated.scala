package golden.framework.hibernate.mapping

enum PropertyGenerated(val value: String):
  case Never extends PropertyGenerated("never")
  case Insert extends PropertyGenerated("insert")
  case Always extends PropertyGenerated("always")
