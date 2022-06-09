package com.mobilejazz.colloc

import com.mobilejazz.colloc.classic.CollocClassicInteractor
import com.mobilejazz.colloc.domain.error.PlatformNotSupported
import com.mobilejazz.colloc.domain.model.Platform
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.net.URL

class CollocClassicInteractorTest {
    @Test
    fun `incorrect platforms returns an error`() {
        assertThrows<PlatformNotSupported> {
            val url =
                URL("https://docs.google.com/spreadsheets/d/13EXpNK62xYm2UiTW-MhNP6eij2GV_vMpmOJNTeYNG7w/export?format=tsv")
            val platform = Platform.ANGULAR
            val output = File("/tmp/output/")
            (CollocClassicInteractor())(url, output, platform)
        }
    }

    @Test
    fun `correct link generates a file`() {
        val url =
            URL("https://docs.google.com/spreadsheets/d/13EXpNK62xYm2UiTW-MhNP6eij2GV_vMpmOJNTeYNG7w/export?format=tsv")
        val platform = Platform.IOS
        val output = File("/tmp/output/")
        val classic = CollocClassicInteractor()
        classic(url, output, platform)
        assert(output.length() > 0)
        output.deleteRecursively()
    }
}
