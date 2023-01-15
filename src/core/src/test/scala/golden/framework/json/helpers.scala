package golden.framework.json

import com.fasterxml.jackson.databind.{Module, ObjectMapper}
import golden.framework.Type

def serializeAsJson(value: Any, module: Module, config: ObjectMapper => ?): String = {
  val mapper = new ObjectMapper()
  Option(config).foreach(_.apply(mapper))
  mapper.registerModule(module)
    .writeValueAsString(value)
}

def deserializeFromJson[T](json: String, tpe: Type, module: Module): T = {
  val mapper = new ObjectMapper()
  val javaType = mapper.constructType(tpe.getType)
  mapper.registerModule(module).readValue[T](json, javaType)
}
