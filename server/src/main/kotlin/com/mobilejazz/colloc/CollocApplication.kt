package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.FormViewInteractor
import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import com.mobilejazz.colloc.domain.model.Platform
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
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
    ): ByteArray? {
        val googleTsvEndPointInteractor = GoogleTsvEndPointInteractor()
        val result = googleTsvEndPointInteractor(id, listOf(platform))

        return result.readBytes()
    }
}

fun main(args: Array<String>) {
    runApplication<CollocApplication>(*args)
}
