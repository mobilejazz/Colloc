package com.mobilejazz.colloc.ext

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class JsonExtensionsKtTest {

  @Test
  fun `assert conversion returns correct value`() {
    val expectedJson =
      ("{" +
      "  \"en\": {" +
      "    \"ls_admin_1\":\"VALUE_1\"," +
      "    \"ls_admin_2\":\"VALUE_2\"" +
      "  }," +
      "  \"es\": {" +
      "    \"ls_admin_1\":\"ES_VALUE_1\"," +
      "    \"ls_admin_2\":\"ES_VALUE_2\"" +
      "  }" +
      "}").filterNot { it.isWhitespace() }

    val actualJson = mapOfAny().toJsonElement().toString()

    assertEquals(expectedJson, actualJson)
  }

  private fun mapOfAny(): Map<Any, Any> {
    return mapOf(
      "en" to mapOf(
        "ls_admin_1" to "VALUE_1",
        "ls_admin_2" to "VALUE_2",
      ),
      "es" to mapOf(
        "ls_admin_1" to "ES_VALUE_1",
        "ls_admin_2" to "ES_VALUE_2"
      )
    )
  }
}
