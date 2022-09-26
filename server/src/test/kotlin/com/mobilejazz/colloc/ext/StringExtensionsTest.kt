package com.mobilejazz.colloc.ext

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringExtensionsTest {

  @Test
  fun `assert literals encoded as required in android`() {
    // Parametrized string
    assertEquals("%sQWE", "%@QWE".encodeAndroidLiterals())

    // Reference literal
    assertEquals("\\@QWE", "@QWE".encodeAndroidLiterals())

    assertEquals("Q@WE", "Q@WE".encodeAndroidLiterals())

    assertEquals("QWE@", "QWE@".encodeAndroidLiterals())

    // Single quote
    assertEquals("\\'QWE", "'QWE".encodeAndroidLiterals())

    // 'And' literal
    assertEquals("&amp;QWE", "&QWE".encodeAndroidLiterals())

    // Closing tag
    assertEquals("&lt;QWE", "<QWE".encodeAndroidLiterals())

    // Opening tag
    assertEquals("&gt;QWE", ">QWE".encodeAndroidLiterals())

    // Double-quote
    assertEquals("\\\"QWE", "\"QWE".encodeAndroidLiterals())

    // Question mark
    assertEquals("\\?QWE", "?QWE".encodeAndroidLiterals())

    assertEquals("Q?WE", "Q?WE".encodeAndroidLiterals())

    assertEquals("QWE?", "QWE?".encodeAndroidLiterals())

  }

  @Test
  fun `assert literals encoded as required in ios`() {
    // Parametrized string
    assertEquals("%@QWE", "%sQWE".encodeIOSLiterals())

    // Double-quote
    assertEquals("\\\"QWE", "\"QWE".encodeIOSLiterals())

  }

  @Test
  fun `assert line separators are removed`() {
    val inputString = "a\\nb" +
      "c"
    val expectedString = "a\\nbc"

    assertEquals(expectedString, inputString.removeLineSeparators())
  }
}
