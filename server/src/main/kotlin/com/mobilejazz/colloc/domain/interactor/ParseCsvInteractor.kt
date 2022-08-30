package com.mobilejazz.colloc.domain.interactor

import com.opencsv.CSVReader
import java.io.FileReader
import java.nio.charset.StandardCharsets

class ParseCsvInteractor {
  operator fun invoke(filePath: String): Map<String, Map<String, String>> {
    val dictionary = mutableMapOf<String, MutableMap<String, String>>()
    var rowIndex = 0
    lateinit var languagePositionMap: Map<String, Int>

    FileReader(filePath, StandardCharsets.UTF_8).use { fr ->
      CSVReader(fr).use { reader ->
        var nextLine = reader.readNext()
        while (nextLine != null) {
          if (rowIndex == 0) {
            languagePositionMap = nextLine.extractLanguagePositionMap()
            languagePositionMap.forEach {
              dictionary[it.key] = mutableMapOf()
            }
          }
          val translationKey = nextLine[0]
          if (rowIndex >= 2 && translationKey.isValid() && nextLine.isTranslationLine()) {
            dictionary.entries.forEach {
              it.value[translationKey] = nextLine[languagePositionMap[it.key]!!]
            }
          }
          nextLine = reader.readNext()
          rowIndex += 1
        }
      }
    }
    return dictionary
  }

  private fun Array<String>.extractLanguagePositionMap(): Map<String, Int> {
    val languagePositionMap = mutableMapOf<String, Int>()
    forEachIndexed { index, s ->
      if (s.isValid())
        languagePositionMap[s] = index
    }
    return languagePositionMap
  }

  private fun String.isValid() = !this.contains("#") && isNotBlank()

  private fun Array<String>.isTranslationLine() = drop(1).joinToString(
    separator = "", prefix = "", postfix = "", limit = -1, truncated = ""
  ).isNotBlank()
}
