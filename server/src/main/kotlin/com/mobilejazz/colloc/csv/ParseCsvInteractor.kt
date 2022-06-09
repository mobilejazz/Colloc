package com.mobilejazz.colloc.csv

import com.opencsv.bean.CsvToBeanBuilder
import java.io.FileReader

class ParseCsvInteractor {
    operator fun invoke(filePath: String): Map<String, Map<String, String>>{
        val translationItemList =  CsvToBeanBuilder<TranslationItem>(FileReader(filePath))
                .withType(TranslationItem::class.java)
                .build()
                .parse()
    }

    private fun List<TranslationItem>.map(): Map<String, Map<String, String>> {
        val languageMap = mutableMapOf<String, Map<String, String>>()
        forEach {
            if (it.key.isAlphanumeric()) {
                val keyTranslationMap = mutableMapOf<String, String>()
                languageMap.getOrPut(, keyTranslationMap)
            }
        }
    }

    private val alphaNumericRegex = "^[a-zA-Z0-9_]*\$".toRegex()
    private fun String.isAlphanumeric() = matches(alphaNumericRegex)
}