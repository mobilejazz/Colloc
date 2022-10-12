package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.CollocInteractor
import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import com.mobilejazz.colloc.domain.model.Platform
import com.mobilejazz.colloc.feature.decoder.CsvLocalizationDecoder
import com.mobilejazz.colloc.feature.encoder.domain.interactor.AndroidEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.AngularEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.IosEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.JsonEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.angular.AngularEncoderV1Interactor
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
      val platform = Platform.values().random()
      assertThrows<CollocInteractor.Error.InvalidIdException> {
        getInteractor()("", 1, platform)
      }
    }
  }

  @Test
  fun `correct id generates a file`() {
    runBlocking {
      val platform = Platform.values().random()
      val id = "1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o"
      val result = getInteractor()(id, 1, platform)
      assert(result.length() > 0)
    }
  }

  private fun provideLocalizationEncodetMap() = mapOf(
    Platform.ANDROID to mapOf(1 to AndroidEncodeInteractor()),
    Platform.IOS to mapOf(1 to IosEncodeInteractor()),
    Platform.ANGULAR to mapOf(
      1 to AngularEncoderV1Interactor(Json {}),
      2 to AngularEncodeInteractor(Json {})
    ),
    Platform.JSON to mapOf(1 to JsonEncodeInteractor(Json {}))
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
      CsvLocalizationDecoder(),
      provideLocalizationEncodetMap()
    )
  }
}
