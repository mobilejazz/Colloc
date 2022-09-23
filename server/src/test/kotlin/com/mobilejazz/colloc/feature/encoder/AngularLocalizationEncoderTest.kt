package com.mobilejazz.colloc.feature.encoder

import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.randomString
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AngularLocalizationEncoderTest{

  @MockK
  private lateinit var json: Json

  @BeforeEach
  fun setUp(){
    MockKAnnotations.init(this)
  }

  @Test
  fun `assert content encoded properly`(){
    val language = Language(code = randomString(),  name = randomString())
    val encodedTranslation = randomString()
    val expectedEncodedLanguageContentMap = mapOf(language to encodedTranslation)
    val rawTranslation = mapOf(
      randomString() to randomString(),
      randomString() to randomString()
    )
    val dictionary = mapOf(language to rawTranslation)
    every { json.encodeToString(any(), rawTranslation) } returns encodedTranslation

    val actualEncodedLanguageContentMap = givenEncoder().encodeContent(dictionary)

    verify(exactly = 1) { json.encodeToString(any(), rawTranslation) }
    assertEquals(expectedEncodedLanguageContentMap, actualEncodedLanguageContentMap)
  }

  @Test
  fun `assert correct content file name returned`(){
    val language = Language(code = randomString(),  name = randomString())
    val expectedFileName = "angular/${language.name}.json"

    val actualFileName = givenEncoder().encodeLocalizationFileName(language)

    assertEquals(expectedFileName, actualFileName)
  }

  private fun givenEncoder() = AngularLocalizationEncoder(json)
}
