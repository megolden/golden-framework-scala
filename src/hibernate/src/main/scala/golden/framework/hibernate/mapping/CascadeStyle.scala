package golden.framework.hibernate.mapping

enum CascadeStyle(val value: String):
  case All extends CascadeStyle("all")
  case AllDeleteOrphan extends CascadeStyle("all-delete-orphan")
  case DeleteOrphan extends CascadeStyle("delete-orphan")
  case Create extends CascadeStyle("create")
  case Evict extends CascadeStyle("evict")
  case Lock extends CascadeStyle("lock")
  case Merge extends CascadeStyle("merge")
  case Persist extends CascadeStyle("persist")
  case Refresh extends CascadeStyle("refresh")
  case Duplicate extends CascadeStyle("duplicate")
  case Delete extends CascadeStyle("delete")
  case None extends CascadeStyle("none")
  case SaveUpdate extends CascadeStyle("save-update")
