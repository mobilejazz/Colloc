package com.mobilejazz.colloc.domain.interactor

import com.mobilejazz.colloc.classic.CollocClassicInteractor
import com.mobilejazz.colloc.csv.ParseCsvInteractor
import com.mobilejazz.colloc.domain.model.Platform
import com.mobilejazz.colloc.file.FileUtils
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
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

  suspend operator fun invoke(link: String, platforms: List<Platform>): File {
    if (platforms.isEmpty()) throw Error.InvalidPlatformException("empty platform list is provided")
    val url = validateLink(link)


    val tempFolder = File("/tmp/" + UUID.randomUUID().toString())

    for (platform in platforms) {
      when (platform) {
        Platform.ANGULAR -> {
          val tsv = downloadTsv(url)
          generateAngularLocales(tsv, tempFolder)
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

  private fun generateAngularLocales(csvFile: File, output: File) {
    val parse = ParseCsvInteractor()
    val result = parse(csvFile.absolutePath)
    val json = Json {  }
    val angularFolder = File(output.absolutePath + "/angular/")
    angularFolder.mkdirs()
    for (content in result) {
      val serializer = MapSerializer(String.serializer(), String.serializer())
      val languageJson = json.encodeToString(serializer, content.value)
      val languageFile = File("${angularFolder.absolutePath}/${content.key}.json")
      languageFile.writeText(languageJson)
    }
  }

  private fun compressFolder(tempFolder: File): File {
    val zippedFile = FileUtils.createFile("/tmp/", UUID.randomUUID().toString() + ".zip")
    FileUtils.generateZip(tempFolder, zippedFile)
    return zippedFile
  }
}
