package com.mobilejazz.colloc.feature.encoder

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.ext.encodeAndroidLiterals

class AndroidLocalizationEncoder : LocalizationEncoder {
  override fun encodeContent(dictionary: Dictionary): Map<Language, String> =
    dictionary
      .mapValues {
        it.value.encode()
      }

  override fun encodeLocalizationFileName(language: Language): String = "values${language.encodeLanguageCode()}/strings.xml"

  private fun Map<String, String>.encode(): String =
    buildString {
      append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
      append("<resources>\n\n")
      append(
        "\t<!--This is an automatically generated file. Please don't modify it.-->\r\n"
      )
      appendTranslations(translation = this@encode)
      append("\n</resources>")
    }

  private fun StringBuilder.appendTranslations(translation: Map<String, String>) = with(translation) {
    entries.forEach {
      append("\n\n")
      if (it.key.isComment()) {
        appendLine()
        append(it.key.encodeCommentLine())
      } else
        append(it.encodeTranslationLine())
    }
  }

  private fun Language.encodeLanguageCode(): String =
    if (code == "en")
      ""
    else
      "-$code"

  private fun Map.Entry<String, String>.encodeTranslationLine(): String =
    "\t<string name=\"$key\">${value.encodeAndroidLiterals()}</string>"

  private fun String.encodeCommentLine(): String = "\t<!--$this-->"

  private fun String.isComment() = startsWith("#")
}
