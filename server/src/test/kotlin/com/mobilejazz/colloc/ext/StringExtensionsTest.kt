package com.mobilejazz.colloc.ext

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringExtensionsTest {

  class Android {
    @Test
    fun assertAllInvalidLiteralsFixed() {
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
  }
}
