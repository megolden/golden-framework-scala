package golden.framework.json

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import com.fasterxml.jackson.databind.SerializationFeature.{WRITE_ENUMS_USING_INDEX, WRITE_ENUM_KEYS_USING_INDEX}
import org.scalatest.Entry
import java.util.{HashMap as JHashMap, Map as JMap}
import golden.framework.typeOf

class EnumModuleTests extends AnyFunSuite with Matchers:

  test("Enums should be serialize as ordinal value") {
    val json = serializeAsJson(Fruit.Orange, EnumModule, _.enable(WRITE_ENUMS_USING_INDEX))

    json should be ("2")
  }

  test("Enums should be serialize as name") {
    val json = serializeAsJson(Fruit.Orange, EnumModule, _.disable(WRITE_ENUMS_USING_INDEX))

    json should be ("\"Orange\"")
  }

  test("Enums should be deserialize by name or ordinal") {
    val banana = deserializeFromJson[Fruit]("1", typeOf[Fruit], EnumModule)
    val orange = deserializeFromJson[Fruit]("\"Orange\"", typeOf[Fruit], EnumModule)

    banana should be (Fruit.Banana)
    orange should be (Fruit.Orange)
  }

  test("Enums should be serialize as object integer key") {
    val map = new JHashMap[Fruit, Int] { put(Fruit.Orange, 100) }

    val json = serializeAsJson(map, EnumModule, _.enable(WRITE_ENUM_KEYS_USING_INDEX))

    json should be ("{\"2\":100}")
  }

  test("Enums should be serialize as object string key") {
    val map = new JHashMap[Fruit, Int] { put(Fruit.Orange, 100) }

    val json = serializeAsJson(map, EnumModule, _.disable(WRITE_ENUM_KEYS_USING_INDEX))

    json should be ("{\"Orange\":100}")
  }

  test("Enums should be deserialize object key by name or ordinal") {
    val banana = deserializeFromJson[JMap[Fruit, Int]]("{\"1\":10}", typeOf[JMap[Fruit, Int]], EnumModule)
    val orange = deserializeFromJson[JMap[Fruit, Int]]("{\"Orange\":100}", typeOf[JMap[Fruit, Int]], EnumModule)

    banana should contain only Entry(Fruit.Banana, 10)
    orange should contain only Entry(Fruit.Orange, 100)
  }

enum Fruit { case Apple, Banana, Orange, Peach, Grape }
