package com.mobilejazz.colloc.domain.interactor

import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.domain.model.Translation
import com.mobilejazz.colloc.feature.encoder.LocalizationEncoder
import com.mobilejazz.colloc.randomString
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class EncodeLocalizationInteractorTest {

  @TempDir
  private val localizationDirectory: File = File("{src/test/resources}/encode_localization/")

  @Test
  fun `assert translations are written to file`() {
    val dictionary = mapOf<Language, Translation>()
    val languageEncodedContentMap = mapOf(
      Language(code = randomString(), name = randomString()) to randomString()
    )
    val encoder = mockk<LocalizationEncoder>()
    val encodeInteractor = EncodeLocalizationInteractor(encoder)
    every { encoder.encodeContent(dictionary) } returnsMany languageEncodedContentMap.toList().map { mapOf(it) }
    languageEncodedContentMap.forEach {
      every { encoder.encodeLocalizationFileName(any()) } returns it.key.code
    }

    encodeInteractor(localizationDirectory, dictionary)

    verify(exactly = 1) { encoder.encodeContent(dictionary) }
    languageEncodedContentMap.forEach {
      verify(exactly = 1) { encoder.encodeLocalizationFileName(it.key) }
      val actualLocalizationFileContent = File(localizationDirectory, it.key.code).readText()
      assertEquals(it.value, actualLocalizationFileContent)
    }
  }
}
