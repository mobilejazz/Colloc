package com.mobilejazz.colloc

import com.mobilejazz.colloc.csv.ParseCsvInteractor
import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URL

class ParseCsvInteractorTest {

  @Test
  fun `should success`() = runBlocking {
    val interactor = ParseCsvInteractor()
    val download = DownloadFileInteractor()
    val url = URL("https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o")
    val file = download(url = url, filename = "output")

//    println(file.length())
//    interactor(file)
    assertEquals(file.length() > 0, true)
  }
}
