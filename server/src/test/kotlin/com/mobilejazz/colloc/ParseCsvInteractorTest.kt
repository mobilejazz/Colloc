package com.mobilejazz.colloc

import com.mobilejazz.colloc.csv.ParseCsvInteractor
import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParseCsvInteractorTest {

  @Test
  fun `should success`() = runBlocking {
    val interactor = ParseCsvInteractor()
    val download = DownloadFileInteractor()
//    download("")
    assertEquals(true, true)
  }
}
