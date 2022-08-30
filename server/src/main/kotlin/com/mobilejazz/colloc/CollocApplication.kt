package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.CollocInteractor
import com.mobilejazz.colloc.domain.model.Platform
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@SpringBootApplication
class CollocApplication

@Controller
class CollocController {

  @Autowired
  lateinit var collocInteractor: CollocInteractor

  @GetMapping("/")
  suspend fun home(): String {
    return "home"
  }

  @GetMapping(
    value = ["/colloc"],
    produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
  )
  suspend fun colloc(
    @RequestParam(value = "id") id: String,
    @RequestParam(value = "platform") platform: Platform,
  ): ResponseEntity<ByteArray>? {
    val result = collocInteractor(id, listOf(platform))
    val bytes = result.readBytes()

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=colloc.zip")
      .contentType(MediaType("application", "zip"))
      .contentLength(bytes.size.toLong())
      .body(bytes)
  }
}

fun main(args: Array<String>) {
  runApplication<CollocApplication>(*args)
}
