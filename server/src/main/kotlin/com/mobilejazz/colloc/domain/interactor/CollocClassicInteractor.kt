package com.mobilejazz.colloc.classic

import java.io.File
import java.net.URL

enum class Platform {
    IOS,
    ANDROID,
    JSON,
}

class CollocClassicInteractor {
    /** Executes the PHP version of Colloc */
    operator fun invoke(url: URL, outputFolder: File, platform: Platform) {
        // map platform to parameter
        val p = when (platform) {
            Platform.IOS -> "100"
            Platform.ANDROID -> "010"
            Platform.JSON -> "001"
        }
        val proc = Runtime.getRuntime().exec(arrayOf("php", "colloc.php", url.toString(), outputFolder.toString(), p))
        val status = proc.waitFor()
        if (status != 0)
            throw Exception("Failed to run Colloc: $status")
    }
}
