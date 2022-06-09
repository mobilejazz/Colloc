package com.mobilejazz.colloc.domain.interactor

import com.mobilejazz.colloc.classic.CollocClassicInteractor
import com.mobilejazz.colloc.classic.Platform
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.UUID

class GoogleTsvEndPointInteractor(
    val downloadFileInteractor: DownloadFileInteractor = DownloadFileInteractor(),
    val collocClassicInteractor: CollocClassicInteractor = CollocClassicInteractor(),
) {
    suspend operator fun invoke(link: String?, platforms: List<Platform>): File? {
        val validatedLinkOrNull = validateLink(link)

        if (validatedLinkOrNull === null) {
            return null
        }

//        val tsv = downloadTsv(validatedLinkOrNull)
        val tempFolder = generateTempFolder()

        for (platform in platforms) {
            collocClassicInteractor(validatedLinkOrNull, tempFolder, platform)
        }

        val zip = compressFolder(tempFolder)
//        deleteTempFolder(tempFolder)

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
    private fun generateTempFolder(): File {
        return File("/tmp/" + UUID.randomUUID().toString())
    }

    suspend private fun downloadTsv(link: URL): File {
        return downloadFileInteractor(link, "downloadedTsv.tsv")
    }

    /**
     * @todo
     */
    private fun compressFolder(tempFolder: File): File {
        return File("")
    }

    private fun deleteTempFolder(tempFolder: File): Boolean {
        return tempFolder.deleteRecursively()
    }
}
