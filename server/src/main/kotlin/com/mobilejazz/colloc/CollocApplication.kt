package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.mobilejazz.colloc.classic.CollocClassicInteractor
import com.mobilejazz.colloc.classic.Platform
import java.io.File
import java.net.URL


@SpringBootApplication
@RestController
class CollocApplication {

    @GetMapping("/hello")
    suspend fun hello(@RequestParam(value = "name", defaultValue = "World") name: String?): String {
        val downloadFileInteractor = DownloadFileInteractor()
        val a = downloadFileInteractor(url = "https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o") println (a.absolutePath)
        println(a.absolutePath)
        return String.format("Hello %s!", name)
    }

    @GetMapping("/classic")
    fun classic(
        @RequestParam(value = "url") url: String?,
        @RequestParam(value = "platform") platform: String?
    ): String {
        val u =
            URL("https://docs.google.com/spreadsheets/d/13EXpNK62xYm2UiTW-MhNP6eij2GV_vMpmOJNTeYNG7w/export?format=tsv")
        val p = Platform.IOS
        val output = File("/tmp/output/")
        val classic = CollocClassicInteractor()
        classic.invoke(u, output, p)
        return "OK"
    }

    @GetMapping("/google-tsv")
    fun googleTsv(@RequestParam(value = "link", defaultValue = "") link: String?): String {
        val result = (GoogleTsvEndPointInteractor())(link)
        return String.format("TSV download link: %s", result)
    }
}

fun main(args: Array<String>) {
    runApplication<CollocApplication>(*args)
}
