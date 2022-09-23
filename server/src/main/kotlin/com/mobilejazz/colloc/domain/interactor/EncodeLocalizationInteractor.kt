package com.mobilejazz.colloc.domain.interactor

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.feature.encoder.LocalizationEncoder
import com.mobilejazz.colloc.ext.createFile
import java.io.File


class EncodeLocalizationInteractor(private val localizationEncoder: LocalizationEncoder) {
  operator fun invoke(outputDirectory: File, dictionary: Dictionary) {
    localizationEncoder.encodeContent(dictionary)
      .forEach { (language, translations) ->
        outputDirectory
          .createDestinationFile(language)
          .writeText(translations)
      }
  }

  private fun File.createDestinationFile(language: Language): File {
    val fileName = "${absolutePath}/${localizationEncoder.encodeLocalizationFileName(language)}"
    return File(fileName).createFile()
  }
}
