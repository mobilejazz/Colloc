package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URL

class DownloadFileInteractorTest {
  @Test
  fun `correct link generates a file`() {
    runBlocking {
      val link =
        "https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o"
      val result = DownloadFileInteractor()(URL(link), "somefilename.txt")
      assert(result is File)
    }
  }
}
