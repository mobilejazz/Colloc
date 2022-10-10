package com.mobilejazz.colloc.di

import com.mobilejazz.colloc.domain.interactor.CollocInteractor
import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import com.mobilejazz.colloc.domain.model.Platform
import com.mobilejazz.colloc.feature.decoder.CsvLocalizationDecoder
import com.mobilejazz.colloc.feature.decoder.LocalizationDecoder
import com.mobilejazz.colloc.feature.encoder.domain.interactor.AndroidEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.AngularEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.EncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.IosEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.JsonEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.angular.AngularEncoderV1Interactor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ApplicationModule {

  @Bean
  fun provideCollocInteractor(
    downloadFileInteractor: DownloadFileInteractor,
    localizationDecoder: LocalizationDecoder,
    platformCollocInteractorMap: Map<Platform, Map<Int, EncodeInteractor>>
  ) =
    CollocInteractor(
      downloadFileInteractor,
      localizationDecoder,
      platformCollocInteractorMap
    )

  @Bean
  fun provideDownloadFileInteractor(httpClient: HttpClient) = DownloadFileInteractor(httpClient)

  @Bean
  fun provideHttpClient() = HttpClient(OkHttp) {
    install(HttpTimeout) {
      requestTimeoutMillis = 60_000
    }
  }


  @Bean
  fun provideLocalizationDecoder() = CsvLocalizationDecoder()

  @Bean
  fun providePlatformEncodeInteractorMap(json: Json) = mapOf(
    Platform.ANDROID to mapOf(1 to AndroidEncodeInteractor()),
    Platform.IOS to mapOf(1 to IosEncodeInteractor()),
    Platform.ANGULAR to mapOf(
      1 to AngularEncoderV1Interactor(json),
      2 to AngularEncodeInteractor(json)
    ),
    Platform.JSON to mapOf(1 to JsonEncodeInteractor(json))
  )

  @Bean
  fun provideJson() = Json { prettyPrint = true }
}

