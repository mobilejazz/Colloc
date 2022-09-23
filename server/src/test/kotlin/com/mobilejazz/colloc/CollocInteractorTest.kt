package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.CollocClassicInteractor
import com.mobilejazz.colloc.domain.interactor.CollocInteractor
import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import com.mobilejazz.colloc.domain.interactor.EncodeLocalizationInteractor
import com.mobilejazz.colloc.domain.model.Platform
import com.mobilejazz.colloc.feature.decoder.CsvLocalizationDecoder
import com.mobilejazz.colloc.feature.encoder.AndroidLocalizationEncoder
import com.mobilejazz.colloc.feature.encoder.AngularLocalizationEncoder
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CollocInteractorTest {

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
      val result = getInteractor()(id, listOf(Platform.ANGULAR, Platform.ANDROID))
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

  private fun provideLocalizationEncodetMap() = mapOf(
    Platform.ANDROID to EncodeLocalizationInteractor(AndroidLocalizationEncoder()),
    Platform.ANGULAR to EncodeLocalizationInteractor(AngularLocalizationEncoder(Json { }))
  )

  private val httpClient =
    HttpClient(OkHttp) {
      install(HttpTimeout) {
        requestTimeoutMillis = 60_000
      }
    }

  private fun getInteractor(): CollocInteractor {
    return CollocInteractor(
      DownloadFileInteractor(httpClient),
      CollocClassicInteractor("/temp"),
      CsvLocalizationDecoder(),
      provideLocalizationEncodetMap()
    )
  }
}
