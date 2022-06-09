package com.mobilejazz.colloc.csv

import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvBindByPosition

data class TranslationItem(
        @CsvBindByName(column = "key")
        val key: String = "",
        @CsvBindByName(column = "English")
        val english: String = "",
        @CsvBindByName(column = "Spanish")
        val spanish: String = ""
)