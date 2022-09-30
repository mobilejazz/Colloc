package com.mobilejazz.colloc.ext

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal fun Any.toJsonElement(): JsonElement =
  when (this) {
    is Map<*, *> -> toJsonElement()
    is String -> JsonPrimitive(this)
    else -> throw IllegalStateException("Can't serialize unknown type: $this")
  }

private fun Map<*, *>.toJsonElement(): JsonElement {
  val map: MutableMap<String, JsonElement> = LinkedHashMap(size)
  forEach { (key, value) ->
    key as String
    when (value) {
      is Map<*, *> -> map[key] = value.toJsonElement()
      is String -> map[key] = JsonPrimitive(value)
      else -> throw IllegalStateException("Can't serialize unknown type: $value")
    }
  }
  return JsonObject(map)
}
