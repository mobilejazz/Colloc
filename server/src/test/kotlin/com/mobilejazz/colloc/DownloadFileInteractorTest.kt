package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import com.mobilejazz.colloc.ext.createFile
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.net.URL

class DownloadFileInteractorTest {

  @TempDir
  private var testDir: File = File("src/test/resources/download_file/")

  @Test
  fun `correct link generates a file`() {
    val file = File("$testDir/somefilename.txt").createFile()
    runBlocking {
      val link =
        "https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o"
      val result = DownloadFileInteractor(httpClient)(URL(link), file)
      assert(result.length() > 0)
    }
  }

  private val httpClient =
    HttpClient(OkHttp) {
      install(HttpTimeout) {
        requestTimeoutMillis = 60_000
      }
    }
}
