package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.ext.createFile
import java.io.File

internal const val DO_NOT_MODIFY_LINE = "This is an automatically generated file. Please don't modify it."

abstract class EncodeInteractor {
  abstract operator fun invoke(outputDirectory: File, dictionary: Dictionary)

  protected fun writeContent(content: String, outputDirectory: File, outputFileName: String) {
    val fileName = "${outputDirectory.absolutePath}/$outputFileName"
    File(fileName)
      .createFile()
      .writeText(content)
  }

  protected fun String.isComment() = startsWith("#")
}
