package com.mobilejazz.colloc.domain.interactor

import com.mobilejazz.colloc.domain.model.Platform
import com.mobilejazz.colloc.feature.decoder.LocalizationDecoder
import com.mobilejazz.colloc.ext.createFile
import com.mobilejazz.colloc.ext.generateZip
import com.mobilejazz.colloc.ext.toFullImportedFileURL
import java.io.File
import java.net.URL
import java.util.UUID

class CollocInteractor(
  private val downloadFileInteractor: DownloadFileInteractor,
  private val collocClassicInteractor: CollocClassicInteractor,
  private val localizationDecoder: LocalizationDecoder,
  private val platformEncodeInteractorMap: Map<Platform, EncodeLocalizationInteractor>
) {
  sealed class Error {
    class InvalidIdException(reason: String) : Exception(reason)
    class InvalidPlatformException(reason: String) : Exception(reason)
  }

  suspend operator fun invoke(id: String, platforms: List<Platform>): File {
    if (id.isEmpty()) throw Error.InvalidIdException("No ID provided")
    if (platforms.isEmpty()) throw Error.InvalidPlatformException("empty platform list is provided")

    val tempFolder = File("/tmp/" + UUID.randomUUID().toString())

    for (platform in platforms) {
      when (platform) {
        Platform.ANGULAR,
        Platform.ANDROID -> platform.generateLocales(id, tempFolder)

        else -> collocClassicInteractor(id, tempFolder, platform)
      }
    }

    val zip = compressFolder(tempFolder)
    tempFolder.deleteRecursively()

    return zip
  }

  private suspend fun Platform.generateLocales(id: String, outputDirectory: File) {
    val csvFile = downloadCsv(id.toFullImportedFileURL())
    val dictionary = localizationDecoder.decode(csvFile.absolutePath)
    platformEncodeInteractorMap.getValue(this).invoke(outputDirectory, dictionary)
  }

  private suspend fun downloadCsv(link: URL): File = downloadFileInteractor(
    link,
    File("downloaded_google_drive_file.csv").createFile()
  )

  private fun compressFolder(tempFolder: File): File {
    val zippedFile = File("/tmp/${UUID.randomUUID()}.zip").createFile()
    tempFolder.generateZip(zippedFile)
    return zippedFile
  }
}
