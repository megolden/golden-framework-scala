package golden.framework.json

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.{Module, ObjectMapper}
import com.fasterxml.jackson.databind.module.SimpleModule

def serializeAsJson(value: Any, setup: SimpleModule => Unit = null): String = {
  val module = new SimpleModule
  Option(setup).tapEach(_.apply(module))
  new ObjectMapper()
    .registerModule(module)
    .writeValueAsString(value)
}
def serializeAsJson(value: Any, module: Module, config: ObjectMapper => Unit): String = {
  val mapper = new ObjectMapper()
  Option(config).tapEach(_.apply(mapper))
  mapper.registerModule(module)
    .writeValueAsString(value)
}

def deserializeFromJson[A](json: String, clazz: Class[A], setup: SimpleModule => Unit = null): A = {
  val module = new SimpleModule
  Option(setup).tapEach(_.apply(module))
  new ObjectMapper()
    .registerModule(module)
    .readValue[A](json, clazz)
}
def deserializeFromJson[A](json: String, clazz: Class[A], module: Module): A = {
  new ObjectMapper()
    .registerModule(module)
    .readValue[A](json, clazz)
}
def deserializeFromJson[A](json: String, typeRef: TypeReference[A], module: Module): A = {
  new ObjectMapper()
    .registerModule(module)
    .readValue[A](json, typeRef)
}
