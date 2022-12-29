package golden.framework.hibernate.mapping

enum IdGenerator(name: String):
  case Increment extends IdGenerator("increment")
  case Identity extends IdGenerator("identity")
  case Foreign extends IdGenerator("foreign")
  case UUID extends IdGenerator("uuid")
  case GUID extends IdGenerator("guid")
  case Assigned extends IdGenerator("assigned")

  override def toString: String = name
