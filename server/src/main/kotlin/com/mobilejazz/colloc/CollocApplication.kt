package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@RestController
class CollocApplication {
    @GetMapping("/hello")
    suspend fun hello(@RequestParam(value = "name", defaultValue = "World") name: String?): String {
        val downloadFileInteractor = DownloadFileInteractor()
        val a = downloadFileInteractor(url = "https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o")
        println(a.absolutePath)
        return String.format("Hello %s!", name)
    }

    @GetMapping("/google-tsv")
    fun googleTsv(@RequestParam(value = "link", defaultValue = "") link: String?): String {
        val result  = (GoogleTsvEndPointInteractor())(link)
        return String.format("TSV download link: %s", result)
    }
}

fun main(args: Array<String>) {
    runApplication<CollocApplication>(*args)
}
