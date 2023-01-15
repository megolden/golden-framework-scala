package golden.framework

import java.lang.reflect.Type as JType

private class TypeImpl(val symbolName: String, val simpleName: String) extends Type:
  override val name: String = symbolName
  override protected def internalGetRawType: Class[?] = Class.forName(symbolName)
  override protected def internalGetType: JType = internalGetRawType
