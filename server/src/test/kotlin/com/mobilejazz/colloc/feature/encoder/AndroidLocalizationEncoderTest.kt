package com.mobilejazz.colloc.feature.encoder

import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.randomString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class AndroidLocalizationEncoderTest{

  @Test
  fun `assert content encoded properly`(){
    val rawTranslation = mapOf(
      "#Generic" to randomString(),
      "ls_key" to "ls_value"
    )
    val encodedTranslation =
      "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
        "\n<resources>" +
        "\n\n\t<!--This is an automatically generated file. Please don't modify it.-->"+
        "\r\n\n\n\n\t<!--#Generic-->" +
        "\n\n\t<string name=\"ls_key\">ls_value</string>" +
        "\n</resources>"
    val language = Language(code = randomString(),  name = randomString())
    val expectedEncodedLanguageContentMap = mapOf(language to encodedTranslation)
    val dictionary = mapOf(language to rawTranslation)

    val actualEncodedLanguageContentMap = givenEncoder().encodeContent(dictionary)

    assertEquals(expectedEncodedLanguageContentMap, actualEncodedLanguageContentMap)
  }

  @Test
  fun `assert correct content file name returned when language is non-english`(){
    val language = Language(code = randomNonEnglishLanguageCode(),  name = randomString())
    val expectedFileName = "values-${language.code}/strings.xml"

    val actualFileName = givenEncoder().encodeLocalizationFileName(language)

    assertEquals(expectedFileName, actualFileName)
  }

  @Test
  fun `assert correct content file name returned when language is english`(){
    val language = Language(code = "en",  name = randomString())
    val expectedFileName = "values/strings.xml"

    val actualFileName = givenEncoder().encodeLocalizationFileName(language)

    assertEquals(expectedFileName, actualFileName)
  }

  private fun givenEncoder() = AndroidLocalizationEncoder()

  private fun randomNonEnglishLanguageCode(): String {
    val string = randomString()
    return if (string.trim() == "en")
      randomNonEnglishLanguageCode()
    else
      string
  }
}
