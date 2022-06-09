package com.mobilejazz.colloc

import com.mobilejazz.colloc.classic.CollocClassicInteractor
import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import com.mobilejazz.colloc.domain.model.Platform
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.net.URL


@SpringBootApplication
@RestController
class CollocApplication {

  @GetMapping("/")
  suspend fun example(@RequestParam(value = "name", defaultValue = "World") name: String?): String {
    return "Format to request /google-tsv?platform=*IOS|ANDROID|JSON|ANGULAR*&link=*GOOGLELINK*"
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

  @GetMapping(
    value =["/google-tsv"],
    produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
  )
  suspend fun googleTsv(
    @RequestParam(value = "link") link: String,
    @RequestParam(value = "platform") platform: Platform
  ): ByteArray? {
    val googleTsvEndPointInteractor = GoogleTsvEndPointInteractor()
    val result = googleTsvEndPointInteractor(link, listOf(Platform.IOS))

    return result?.readBytes()
  }
}

fun main(args: Array<String>) {
  runApplication<CollocApplication>(*args)
}
