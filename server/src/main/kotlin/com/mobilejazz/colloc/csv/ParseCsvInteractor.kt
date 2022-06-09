package com.mobilejazz.colloc.csv

import com.opencsv.CSVReader
import java.io.FileReader
import java.nio.charset.StandardCharsets


class ParseCsvInteractor {
    operator fun invoke(filePath: String): Map<String, Map<String, String>> {
        val map = mutableMapOf<String, MutableMap<String, String>>()
        var isFirstLine = true
        lateinit var languagePositionMap : Map<String, Int>

        FileReader(filePath, StandardCharsets.UTF_8).use { fr ->
            CSVReader(fr).use { reader ->
                var nextLine = reader.readNext()
                while (nextLine != null) {
                    if (isFirstLine) {
                        languagePositionMap = nextLine.extractLanguagePositions()
                        languagePositionMap.forEach {
                            map[it.key] = mutableMapOf()
                        }
                        isFirstLine = false
                    }
                    map.entries.forEach {
                        it.value[nextLine[0]] = nextLine[languagePositionMap[it.key]!!]
                    }
                    nextLine = reader.readNext()
                }
            }
        }
        return map
    }

    private fun Array<String>.extractLanguagePositions(): Map<String, Int> {
        val positionList = mutableMapOf<String, Int>()
        forEachIndexed { index, s ->
            if (s.isValid())
                positionList[s] = index
        }
        return positionList
    }

    private fun String.isValid() = this != "#" && isNotEmpty()
}