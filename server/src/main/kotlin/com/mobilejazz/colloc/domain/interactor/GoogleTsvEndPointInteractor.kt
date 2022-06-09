package com.mobilejazz.colloc.domain.interactor

import java.io.File
import java.net.MalformedURLException
import java.net.URL

class GoogleTsvEndPointInteractor {
    operator fun invoke(link: String?): File? {
        val validatedLinkOrNull = isLinkValid(link)

        if (validatedLinkOrNull === null) {
            return null
        }

        val tsv = downloadTsv(validatedLinkOrNull)
        val tempFolder = generateTempFolder()

        generateJson(tsv, tempFolder)
        val zip = compressFolder(tempFolder)
        deleteTempFolder(tempFolder)

        return zip
    }

    private fun isLinkValid(link: String?): URL? {
        if (link === null) {
            return null
        }

        try {
            val url = URL(link)
            url.toURI()
            return url
        } catch (e: MalformedURLException) {
            return null
        }
    }

    /**
     * @todo
     */
    private fun generateTempFolder(): String {
        return ""
    }

    /**
     * @todo
     */
    private fun downloadTsv(link: URL): File {
        return File("")
    }

    /**
     * @todo
     */
    private fun generateJson(tsv: File, tempFolder: String): Boolean {
        return true
    }

    /**
     * @todo
     */
    private fun compressFolder(tempFolder: String): File {
        return File("")
    }

    /**
     * @todo
     */
    private fun deleteTempFolder(tempFolder: String): Boolean {
        return true
    }
}
