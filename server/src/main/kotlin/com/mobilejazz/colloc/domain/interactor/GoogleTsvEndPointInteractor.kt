package com.mobilejazz.colloc.domain.interactor

import java.io.File
import java.net.MalformedURLException
import java.net.URL

class GoogleTsvEndPointInteractor {
    operator fun invoke(link: String?): File? {
        val validatedLinkOrNull = validateLink(link)

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

    private fun validateLink(link: String?): URL? {
        if (link === null) {
            return null
        }

        val url = try {
            val url = URL(link)
            url.toURI()
            url
        } catch (e: MalformedURLException) {
            return null
        }

        val domain = url.host
        val path = url.path

        if (
            !domain.startsWith("docs.google.com")
            || !path.contains("spreadsheets")
        ) {
            return null
        }

        return url
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
