package golden.framework.hibernate.mapping

enum DeleteBehavior(val value: String):
  case NoAction extends DeleteBehavior("noaction")
  case Cascade extends DeleteBehavior("cascade")
