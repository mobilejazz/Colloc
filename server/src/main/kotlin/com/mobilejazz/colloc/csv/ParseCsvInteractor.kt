package com.mobilejazz.colloc.csv

import com.opencsv.CSVReader
import java.io.FileReader
import java.nio.charset.StandardCharsets


class ParseCsvInteractor {
    operator fun invoke(filePath: String): Map<String, Map<String, String>> {
//        val translationItemList =  CsvToBeanBuilder<TranslationItem>(FileReader(filePath))
//                .withType(TranslationItem::class.java)
//                .build()
//                .parse()

        val map = mutableMapOf<String, MutableMap<String, String>>()
        var isFirstLine = true

        lateinit var languagePositions: List<Int>
        FileReader(filePath, StandardCharsets.UTF_8).use { fr ->
            CSVReader(fr).use { reader ->
                var nextLine = reader.readNext()
                while (nextLine != null) {
                    if (isFirstLine) {
                        languagePositions = nextLine.extractLanguagePositions()
                        languagePositions.forEach {
                            map[nextLine[it]] = mutableMapOf()
                        }
                        isFirstLine = false
                    }
                    map.entries.forEach {
                        languagePositions.forEach { pos ->
                            it.value[nextLine[0]] = nextLine[pos]
                        }
                    }
//                    println(nextLine.contentToString())
                    nextLine = reader.readNext()
                }
            }
        }
        return map
    }

    private fun Array<String>.extractLanguagePositions(): List<Int> {
        val positionList = mutableListOf<Int>()
        forEachIndexed { index, s ->
            if (s.isAlphanumeric())
                positionList.add(index)
        }
        return positionList
    }

//    private fun List<TranslationItem>.map(): Map<String, Map<String, String>> {
//        val languageMap = mutableMapOf<String, Map<String, String>>()
//        forEach {
//            if (it.key.isAlphanumeric()) {
//                val keyTranslationMap = mutableMapOf<String, String>()
//                languageMap.getOrPut(, keyTranslationMap)
//            }
//        }
//    }
//
    private val alphaNumericRegex = "^[a-zA-Z0-9_]*\$".toRegex()
    private fun String.isAlphanumeric() = matches(alphaNumericRegex)
}