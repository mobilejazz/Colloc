package com.mobilejazz.colloc.domain.interactor

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File
import java.net.URL

class DownloadFileInteractor(private val client: HttpClient) {
  suspend operator fun invoke(url: URL, destination: File): File {
    val request = client.get(url.toString()) {}
    val readingChannel = request.bodyAsChannel()
    readingChannel.copyAndClose(destination.writeChannel())
    return destination
  }
}
