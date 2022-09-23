package com.mobilejazz.colloc.feature.decoder

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.domain.model.Language
import com.opencsv.CSVReader
import java.io.FileReader
import java.nio.charset.StandardCharsets

private typealias MutableDictionary = MutableMap<Language, MutableMap<String, String>>

class CsvLocalizationDecoder : LocalizationDecoder {
  override fun decode(source: String): Dictionary {

    val fileReader = FileReader(source, StandardCharsets.UTF_8)
    val csvReader = CSVReader(fileReader)
    lateinit var mutableDictionary: MutableDictionary

    lateinit var languageNameList: List<String>
    lateinit var languageCodeList: List<String>
    csvReader.forEachIndexed { rowIndex, row ->
      // First row is reserved for language names
      if (rowIndex == 0)
        languageNameList = row.toList()
      // Second line is reserved for language codes
      if (rowIndex == 1) {
        languageCodeList = row.toList()
        mutableDictionary = initializeDictionaryEditor(languageNameList, languageCodeList)
      }

      val firstElement = row[0].trim()
      if (rowIndex >= 2 && firstElement.isKeyOrComment()) {
        mutableDictionary.entries.forEachIndexed DictionaryTraversal@{ index, entry ->
          val languageName = entry.key.name
          // `#` means this column will not be translated.
          if (languageName == "#")
            return@DictionaryTraversal

          entry.value[firstElement] = row[index + 1]
        }
      }
    }

    return mutableDictionary.entries
      .filter { it.key.name != "#" }
      .associate { it.key to it.value.toMap() }
  }

  private fun initializeDictionaryEditor(languageNameList: List<String>, languageCodeList: List<String>): MutableDictionary {
    val dictionaryEditor = mutableMapOf<Language, MutableMap<String, String>>()
    languageNameList.forEachIndexed { index, languageName ->
      // First column in template is reserved for key, so we skip it
      if (index == 0)
        return@forEachIndexed

      val language = Language(code = languageCodeList[index].trim(), languageName.trim())
      dictionaryEditor[language] = mutableMapOf()
    }
    return dictionaryEditor
  }

  private fun String.isKeyOrComment() = startsWith("#") || isNotBlank()
}
