package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.randomString
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class JsonEncodeInteractorTest {

  @TempDir
  private val localizationDirectory: File = File("{src/test/resources}/encode_localization/")

  @Test
  fun `assert correct content is generated`() {
    val json = mockk<Json>()
    val encodeInteractor = JsonEncodeInteractor(json)
    val expectedTranslation = randomString()
    val englishTranslation = mapOf(
      "#Generic_1" to "        ",
      "ls_generic_accept_1" to "Ok      ",
      "ls_generic_cancel_1" to "Cancel  ",
      "#Generic_2" to "        ",
      "ls_generic_accept_2" to "Ok      ",
      "ls_generic_cancel_2" to "Cancel  "
    )
    val spanishTranslation = mapOf(
      "#Generic_1" to "          ",
      "ls_generic_accept_1" to "Aceptar   ",
      "ls_generic_cancel_1" to "Cancelar  ",
      "#Generic_2" to "          ",
      "ls_generic_accept_2" to "Aceptar   ",
      "ls_generic_cancel_2" to "Cancelar    "
    )
    val dictionary = mapOf(
      Language("en", "English") to englishTranslation,
      Language("es", "Spanish") to spanishTranslation
    )
    val dictionaryToBeEncoded = mapOf(
      "en" to englishTranslation.removeComments(),
      "es" to spanishTranslation.removeComments()
    )
    every { json.encodeToString(any(), dictionaryToBeEncoded) } returns expectedTranslation

    encodeInteractor(localizationDirectory, dictionary)

    verify(exactly = 1) { json.encodeToString(any(), dictionaryToBeEncoded) }
    val actualTranslation = File(localizationDirectory, "stringsFromApp.json").readText()
    Assertions.assertEquals(expectedTranslation, actualTranslation)
  }

  private fun Map<String, String>.removeComments() = filterKeys { !it.startsWith("#") }
}
