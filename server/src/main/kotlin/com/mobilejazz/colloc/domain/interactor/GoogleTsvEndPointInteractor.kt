package com.mobilejazz.colloc.domain.interactor

import com.mobilejazz.colloc.classic.CollocClassicInteractor
import com.mobilejazz.colloc.domain.model.Platform
import com.mobilejazz.colloc.file.FileUtils
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.UUID

class GoogleTsvEndPointInteractor(
  val downloadFileInteractor: DownloadFileInteractor = DownloadFileInteractor(),
  val collocClassicInteractor: CollocClassicInteractor = CollocClassicInteractor(),
) {

  sealed class Error {
    object InvalidURLException : Exception()
    class InvalidPlatformException(reason: String) : Exception(reason)
  }

  suspend operator fun invoke(link: String, platforms: List<Platform>): File? {
    if (platforms.isEmpty()) throw Error.InvalidPlatformException("empty platform list is provided")
    val url = validateLink(link)


    val tempFolder = File("/tmp/" + UUID.randomUUID().toString())

    for (platform in platforms) {
      when (platform) {
        Platform.ANGULAR -> {
          val tsv = downloadTsv(url)
          generateAngularLocales(tempFolder)
        }
        else -> {
          collocClassicInteractor(url, tempFolder, platform)
        }
      }
    }

    val zip = compressFolder(tempFolder)
    tempFolder.deleteRecursively()

    return zip
  }

  private fun validateLink(link: String): URL {
    val url = try {
      URL(link).toURI()
    } catch (e: MalformedURLException) {
      throw Error.InvalidURLException
    }

    if (!url.host.startsWith("docs.google.com") || !url.path.contains("spreadsheets")) {
      throw Error.InvalidURLException
    }
    return url.toURL()
  }

  private suspend fun downloadTsv(link: URL): File = downloadFileInteractor(link, "downloadedTsv.tsv")

  private fun generateAngularLocales(tempFolder: File) {
    // waiting angular generation
  }

  private fun compressFolder(tempFolder: File): File {
    val zippedFile = FileUtils.createFile("/tmp/", UUID.randomUUID().toString() + ".zip")
    FileUtils.generateZip(tempFolder, zippedFile)
    return zippedFile
  }
}
