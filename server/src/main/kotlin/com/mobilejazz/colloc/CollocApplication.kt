package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.FormViewInteractor
import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import com.mobilejazz.colloc.domain.model.Platform
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@RestController
class CollocApplication {
    @GetMapping("/")
    suspend fun example(@RequestParam(value = "name", defaultValue = "World") name: String?): String {
        val html = FormViewInteractor()()
        return html
//        return "Format to request /colloc?platform=*IOS|ANDROID|JSON|ANGULAR*&link=*GOOGLELINK*"
    }

    @GetMapping(
        value = ["/colloc"],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    suspend fun colloc(
        @RequestParam(value = "id") id: String,
        @RequestParam(value = "platform") platform: Platform,
    ): ResponseEntity<ByteArray> {
        val googleTsvEndPointInteractor = GoogleTsvEndPointInteractor()
        val result = googleTsvEndPointInteractor(id, listOf(platform))
        val bytes = result.readBytes()

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=colloc.zip")
            .contentType(MediaType("application","zip"))
            .contentLength(bytes.size.toLong())
            .body(bytes);
    }

    @GetMapping("/error")
    fun error(): String? {
        return "Error handling"
    }

    @Override
    fun getErrorPath(): String? {
        return "/error"
    }
}

fun main(args: Array<String>) {
    runApplication<CollocApplication>(*args)
}
