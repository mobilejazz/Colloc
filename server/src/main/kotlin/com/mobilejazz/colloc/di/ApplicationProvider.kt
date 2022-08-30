package com.mobilejazz.colloc.di

import com.mobilejazz.colloc.domain.interactor.CollocClassicInteractor
import com.mobilejazz.colloc.domain.interactor.CollocInteractor
import com.mobilejazz.colloc.domain.interactor.DownloadFileInteractor
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
  fun provideCollocInteractor(downloadFileInteractor: DownloadFileInteractor, collocClassicInteractor: CollocClassicInteractor) =
    CollocInteractor(downloadFileInteractor, collocClassicInteractor)

  @Bean
  fun provideDownloadFileInteractor() = DownloadFileInteractor()

  private fun Environment.isLocal() = activeProfiles.first().contains("local")
}

