package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.CollocClassicInteractor
import com.mobilejazz.colloc.domain.interactor.CollocInteractor
import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import com.mobilejazz.colloc.domain.model.Platform
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class CollocInteractorTest {
  private fun getInteractor(): CollocInteractor {
    return CollocInteractor(
      DownloadFileInteractor(),
      CollocClassicInteractor("/temp"),
    )
  }

  @Test
  fun `no id returns null`() {
    runBlocking {
      assertThrows<CollocInteractor.Error.InvalidIdException> {
        getInteractor()("", listOf(Platform.IOS))
      }
    }
  }

  @Test
  fun `correct id generates a file`() {
    runBlocking {
      val id = "1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o"
      val result = getInteractor()(id, listOf(Platform.ANGULAR))
      assert(result.length() > 0)
    }
  }

  @Test
  fun `no platforms returns an error`() {
    runBlocking {
      assertThrows<CollocInteractor.Error.InvalidPlatformException> {
        getInteractor()("some random string", listOf())
      }
    }
  }
}
