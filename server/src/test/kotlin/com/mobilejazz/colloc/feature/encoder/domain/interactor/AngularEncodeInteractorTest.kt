package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.ext.toJsonElement
import com.mobilejazz.colloc.randomString
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class AngularEncodeInteractorTest {

  @TempDir
  private val localizationDirectory: File = File("{src/test/resources}/encode_localization/")

  @MockK
  private lateinit var json: Json

  @BeforeEach
  fun setUp() {
    MockKAnnotations.init(this)
  }

  @Test
  fun `assert content encoded properly`() {
    val language = Language(code = randomString(), name = randomString())
    val expectedTranslation = randomString()
    val rawTranslation = anyTranslation()
    val dictionary = mapOf(language to rawTranslation)
    val translationHierarchy = rawTranslation.mapValues {
      it.value.toJsonElement()
    }
    every { json.encodeToString(any(), translationHierarchy) } returns expectedTranslation

    givenEncodeInteractor()(localizationDirectory, dictionary)

    verify(exactly = 1) { json.encodeToString(any(), translationHierarchy) }
    val actualTranslation = File(localizationDirectory, "angular/${language.code}.json").readText()
    assertEquals(expectedTranslation, actualTranslation)
  }

  private fun givenEncodeInteractor() = AngularEncodeInteractor(json)
}
