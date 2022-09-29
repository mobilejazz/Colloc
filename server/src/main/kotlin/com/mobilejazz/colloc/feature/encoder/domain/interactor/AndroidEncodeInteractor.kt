package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.ext.encodeAndroidLiterals
import java.io.File

class AndroidEncodeInteractor : EncodeInteractor() {
  override fun invoke(outputDirectory: File, dictionary: Dictionary) {
    dictionary.forEach {
      writeContent(content = it.value.encode(), outputDirectory, outputFileName = localizationFileName(it.key))
    }
  }

  private fun localizationFileName(language: Language): String = "values${language.encodeLanguageCode()}/strings.xml"

  private fun Map<String, String>.encode(): String =
    buildString {
      append(
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
          "\n<resources>\n\n" +
          "\t<!--$DO_NOT_MODIFY_LINE-->\r\n"
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
}
