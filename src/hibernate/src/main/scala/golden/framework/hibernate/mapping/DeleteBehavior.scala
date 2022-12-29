package golden.framework.hibernate.mapping

enum DeleteBehavior(name: String):
  case NoAction extends DeleteBehavior("noaction")
  case Cascade extends DeleteBehavior("cascade")

  override def toString: String = name
