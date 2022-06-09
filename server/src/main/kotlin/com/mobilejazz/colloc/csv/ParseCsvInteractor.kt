package com.mobilejazz.colloc.csv

import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader


class ParseCsvInteractor {
    operator fun invoke(file: File): Map<String, Map<String, String>>{
        val reader = CSVReader(FileReader(file))
        var nextLine: Array<String>
        while (reader.readNext().also { nextLine = it } != null) {
            // nextLine[] is an array of values from the line
            println(nextLine[0] + nextLine[1] + "etc...")
        }

        return emptyMap()
//        val translationItemList =  CsvToBeanBuilder<TranslationItem>(FileReader(filePath))
//                .withType(TranslationItem::class.java)
//                .build()
//                .parse()
    }

//    private fun List<TranslationItem>.map(): Map<String, Map<String, String>> {
//        val languageMap = mutableMapOf<String, Map<String, String>>()
////        forEach {
////            if (it.key.isAlphanumeric()) {
////                val keyTranslationMap = mutableMapOf<String, String>()
////                languageMap.getOrPut(, keyTranslationMap)
////            }
////        }
//    }

    private val alphaNumericRegex = "^[a-zA-Z0-9_]*\$".toRegex()
    private fun String.isAlphanumeric() = matches(alphaNumericRegex)
}
