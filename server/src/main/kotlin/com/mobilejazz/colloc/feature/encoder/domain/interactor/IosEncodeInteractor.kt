package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.ext.encodeIOSLiterals
import java.io.File

internal const val IOS_DO_NOT_MODIFY_LINE =
  "///" +
    "\n//$DO_NOT_MODIFY_LINE" +
    "\n///\n"

class IosEncodeInteractor : EncodeInteractor() {

  private var isSwiftAndHeaderFileGenerated = false

  override fun invoke(outputDirectory: File, dictionary: Dictionary) {
    dictionary.forEach {
      if (!isSwiftAndHeaderFileGenerated)
        it.value.keys.generateSwiftAndHeaderFiles(outputDirectory)
      writeContent(content = it.value.encodeTranslation(), outputDirectory, outputFileName = localizationFileName(it.key))
    }
  }

  private fun Map<String, String>.encodeTranslation() = buildString {
    append(IOS_DO_NOT_MODIFY_LINE)
    appendTranslations(translation = this@encodeTranslation)
  }

  private fun StringBuilder.appendTranslations(translation: Map<String, String>) = with(translation) {
    entries.forEach {
      append("\n")
      if (it.key.isComment()) {
        appendLine()
        append(it.key.encodeCommentLine())
      } else
        append(it.encodeTranslationLine())
    }
  }

  private fun Map.Entry<String, String>.encodeTranslationLine(): String = "\"$key\" = \"${value.encodeIOSLiterals()}\";"

  private fun String.encodeCommentLine(): String = "// $this"

  private fun Set<String>.generateSwiftAndHeaderFiles(outputDirectory: File) {
    isSwiftAndHeaderFileGenerated = true
    val headerContentBuilder = StringBuilder(IOS_DO_NOT_MODIFY_LINE)
    val swiftContentBuilder = StringBuilder(
      IOS_DO_NOT_MODIFY_LINE +
        "\nimport Foundation" +
        "\n\npublic protocol LocalizedEnum: CustomStringConvertible {}" +
        "\n\nextension LocalizedEnum where Self: RawRepresentable, Self.RawValue == String {" +
        "\n\tpublic var description: String  {" +
        "\n\t\tNSLocalizedString(self.rawValue, comment: \"\")" +
        "\n\t}" +
        "\n}" +
        "\n\npublic enum Colloc: String, LocalizedEnum {"
    )

    forEach {
      if (!it.isComment()) {
        headerContentBuilder.append(it.encodeHeaderLine())
        swiftContentBuilder.append(it.encodeSwiftLine())
      }
    }
    swiftContentBuilder.append("\n}")

    writeContent(content = headerContentBuilder.toString(), outputDirectory, outputFileName = "Localization.h")
    writeContent(content = swiftContentBuilder.toString(), outputDirectory, outputFileName = "Colloc.swift")
  }

  private fun String.encodeHeaderLine() = "\n#define $this NSLocalizedString(@\"$this\", nil)"

  private fun String.encodeSwiftLine() = "\n\tcase $this"

  private fun String.isComment() = startsWith("#")

  private fun localizationFileName(language: Language): String = "${language.code}.lproj/Localizable.strings"
}