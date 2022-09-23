package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.error.PlatformNotSupported
import com.mobilejazz.colloc.domain.interactor.CollocClassicInteractor
import com.mobilejazz.colloc.domain.model.Platform
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.File

class CollocClassicInteractorTest {

  // fixme: must to be provided by the @Bean in the application
  private val collocScriptPath by lazy {
    val filename = "colloc.php"
    val projectDir = System.getProperty("user.dir") // pointing to /server folder
    val collocFolder = File(projectDir).parent // pointing to /Colloc folder
    "$collocFolder/$filename"
  }

  @Test
  fun `incorrect platforms returns an error`() {
    assertThatThrownBy {
      val id = "13EXpNK62xYm2UiTW-MhNP6eij2GV_vMpmOJNTeYNG7w"
      val platform = Platform.ANGULAR
      val output = File("/tmp/output/")
      val collocClassicInteractor = CollocClassicInteractor(collocScriptPath)
      collocClassicInteractor(id, output, platform)
    }.isInstanceOf(PlatformNotSupported::class.java)
  }

  @Test
  fun `correct link generates a file`() {
    val id = "13EXpNK62xYm2UiTW-MhNP6eij2GV_vMpmOJNTeYNG7w"
    val platform = Platform.IOS
    val output = File("/tmp/output/")

    val collocClassicInteractor = CollocClassicInteractor(collocScriptPath = collocScriptPath)
    collocClassicInteractor(id, output, platform)
    assertThat(output.length()).isGreaterThan(0)
    output.deleteRecursively()
  }
}
