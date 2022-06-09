package com.mobilejazz.colloc.csv

import com.opencsv.CSVReader
import java.io.FileReader
import java.nio.charset.StandardCharsets


class ParseCsvInteractor {
    operator fun invoke(filePath: String): Map<String, Map<String, String>>{
//        val translationItemList =  CsvToBeanBuilder<TranslationItem>(FileReader(filePath))
//                .withType(TranslationItem::class.java)
//                .build()
//                .parse()

        FileReader(filePath, StandardCharsets.UTF_8).use { fr ->
            CSVReader(fr).use { reader ->
                var nextLine = reader.readNext()
                while (nextLine != null) {
                    println(nextLine.contentToString())
                    nextLine = reader.readNext()
                }
            }
        }
        return emptyMap()
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
//    private val alphaNumericRegex = "^[a-zA-Z0-9_]*\$".toRegex()
//    private fun String.isAlphanumeric() = matches(alphaNumericRegex)
}