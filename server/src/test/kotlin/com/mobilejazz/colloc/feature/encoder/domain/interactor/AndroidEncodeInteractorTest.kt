package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.feature.encoder.domain.interactor.AndroidEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.DO_NOT_MODIFY_LINE
import com.mobilejazz.colloc.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class AndroidEncodeInteractorTest {

  @TempDir
  private var localizationDirectory: File = File("{src/test/resources}/encode_localization/")

  @Test
  fun `assert content encoded properly`() {
    val rawTranslation = mapOf(
      "#Generic" to randomString(),
      "ls_key" to "ls_value"
    )
    val expectedTranslation =
      "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
        "\n<resources>" +
        "\n\n\t<!--$DO_NOT_MODIFY_LINE-->" +
        "\r\n\n\n\n\t<!--#Generic-->" +
        "\n\n\t<string name=\"ls_key\">ls_value</string>" +
        "\n</resources>"
    val language = Language(code = randomNonEnglishLanguageCode(), name = randomString())
    val dictionary = mapOf(language to rawTranslation)

    givenEncodeInteractor()(localizationDirectory, dictionary)

    val actualTranslation = File(localizationDirectory, "values-${language.code}/strings.xml").readText()
    assertEquals(expectedTranslation, actualTranslation)
  }

  @Test
  fun `assert correct content file name returned when language is non-english`() {
    val language = Language(code = randomNonEnglishLanguageCode(), name = randomString())
    val dictionary = mapOf(language to anyTranslation())

    givenEncodeInteractor()(localizationDirectory, dictionary)

    assertThat(File("values-${language.code}/strings.xml").exists())
  }

  @Test
  fun `assert correct content file name returned when language is english`() {
    val language = Language(code = "en", name = randomString())
    val dictionary = mapOf(language to anyTranslation())

    givenEncodeInteractor()(localizationDirectory, dictionary)

    assertThat(File("${language.code}/strings.xml").exists())
  }

  private fun givenEncodeInteractor() = AndroidEncodeInteractor()

  private fun randomNonEnglishLanguageCode(): String {
    val string = randomString()
    return if (string.trim() == "en")
      randomNonEnglishLanguageCode()
    else
      string
  }
}
