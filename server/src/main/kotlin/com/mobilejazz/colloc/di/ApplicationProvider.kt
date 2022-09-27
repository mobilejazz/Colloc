package com.mobilejazz.colloc.di

import com.mobilejazz.colloc.domain.interactor.CollocClassicInteractor
import com.mobilejazz.colloc.domain.interactor.CollocInteractor
import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
import com.mobilejazz.colloc.domain.model.Platform
import com.mobilejazz.colloc.feature.decoder.CsvLocalizationDecoder
import com.mobilejazz.colloc.feature.decoder.LocalizationDecoder
import com.mobilejazz.colloc.feature.encoder.domain.interactor.AndroidEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.AngularEncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.EncodeInteractor
import com.mobilejazz.colloc.feature.encoder.domain.interactor.IosEncodeInteractor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.io.File


@Configuration
class ApplicationModule {

  @Autowired
  lateinit var enviroment: Environment

  private val collocScriptPath by lazy {
    val filename = "colloc.php"
    if (enviroment.isLocal()) {
      val projectDir = System.getProperty("user.dir") // pointing to /server folder
      val collocFolder = File(projectDir).parent // pointing to /Colloc folder
      "$collocFolder/$filename"
    } else {
      filename
    }
  }

  @Bean
  fun provideCollocClassicInteractor() = CollocClassicInteractor(collocScriptPath)

  @Bean
  fun provideCollocInteractor(
    downloadFileInteractor: DownloadFileInteractor,
    collocClassicInteractor: CollocClassicInteractor,
    localizationDecoder: LocalizationDecoder,
    platformCollocInteractorMap: Map<Platform, EncodeInteractor>
  ) =
    CollocInteractor(
      downloadFileInteractor,
      collocClassicInteractor,
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
    Platform.ANDROID to AndroidEncodeInteractor(),
    Platform.IOS to IosEncodeInteractor(),
    Platform.ANGULAR to AngularEncodeInteractor(json)
  )

  @Bean
  fun provideJson() = Json { }

  private fun Environment.isLocal() = activeProfiles.first().contains("local")
}

