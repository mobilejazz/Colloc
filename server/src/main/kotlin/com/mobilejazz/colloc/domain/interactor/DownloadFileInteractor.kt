package com.mobilejazz.colloc.domain.interactor

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File

class DownloadFileInteractor {

  suspend operator fun invoke(url: String, filename: String): File {
    val client = HttpClient(OkHttp) {
      install(HttpTimeout) {
        requestTimeoutMillis = 60_000
      }
    }
    val file = File("$filename.tsv")
    val request = client.get(url) {}
    val readingChannel = request.bodyAsChannel()
    readingChannel.copyAndClose(file.writeChannel())
    return file
  }
}
