package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.randomString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class IosEncodeInteractorTest {

  @TempDir
  private var localizationDirectory: File = File("{src/test/resources}/encode_localization/")

  @Test
  fun `assert content encoded properly`() {
    val rawTranslation = mapOf(
      "#Generic" to randomString(),
      "ls_key" to "ls_value",
      "ls_key_2" to "ls_value_2"
    )

    val language = Language(code = randomString(), name = randomString())
    val dictionary = mapOf(language to rawTranslation)

    givenEncodeInteractor()(localizationDirectory, dictionary)

    val actualTranslation = File(localizationDirectory, "${language.code}.lproj/Localizable.strings").readText()
    assertActualExpectedTranslationsMatch(actualTranslation)
    assertActualExpectedSwiftFileContentMatches()
    assertActualExpectedHeaderFileContentMatches()
  }

  private fun assertActualExpectedTranslationsMatch(actualTranslation: String) {
    val expectedTranslation =
      IOS_DO_NOT_MODIFY_LINE +
        "\n\n// #Generic" +
        "\n\"ls_key\" = \"ls_value\";" +
        "\n\"ls_key_2\" = \"ls_value_2\";"
    assertEquals(expectedTranslation, actualTranslation)
  }

  private fun assertActualExpectedSwiftFileContentMatches() {
    val expectedContent =
      IOS_DO_NOT_MODIFY_LINE +
        "\nimport Foundation" +
        "\n\npublic protocol LocalizedEnum: CustomStringConvertible {}" +
        "\n\nextension LocalizedEnum where Self: RawRepresentable, Self.RawValue == String {" +
        "\n\tpublic var description: String  {" +
        "\n\t\tNSLocalizedString(rawValue, comment: \"\")" +
        "\n\t}" +
        "\n}" +
        "\n\npublic enum Colloc: String, LocalizedEnum {" +
        "\n\tcase ls_key" +
        "\n\tcase ls_key_2" +
        "\n}"
    val actualContent = File(localizationDirectory, "Colloc.swift").readText()
    assertEquals(expectedContent, actualContent)
  }

  private fun assertActualExpectedHeaderFileContentMatches() {
    val expectedContent =
      IOS_DO_NOT_MODIFY_LINE +
        "\n#define ls_key NSLocalizedString(@\"ls_key\", nil)" +
        "\n#define ls_key_2 NSLocalizedString(@\"ls_key_2\", nil)"
    val actualContent = File(localizationDirectory, "Localization.h").readText()
    assertEquals(expectedContent, actualContent)
  }

  private fun givenEncodeInteractor() = IosEncodeInteractor()
}
