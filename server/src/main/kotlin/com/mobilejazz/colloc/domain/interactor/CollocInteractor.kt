package com.mobilejazz.colloc.domain.interactor

import com.mobilejazz.colloc.domain.model.Platform
import com.mobilejazz.colloc.ext.createFile
import com.mobilejazz.colloc.ext.generateZip
import com.mobilejazz.colloc.ext.toFullImportedFileURL
import com.mobilejazz.colloc.feature.decoder.LocalizationDecoder
import com.mobilejazz.colloc.feature.encoder.domain.interactor.EncodeInteractor
import java.io.File
import java.net.URL
import java.util.UUID

class CollocInteractor(
  private val downloadFileInteractor: DownloadFileInteractor,
  private val localizationDecoder: LocalizationDecoder,
  private val platformEncodeInteractorMap: Map<Platform, Map<Int, EncodeInteractor>>
) {
  sealed class Error {
    class InvalidIdException(reason: String) : Exception(reason)
  }

  suspend operator fun invoke(id: String, version: Int, platform: Platform): File {
    if (id.isBlank()) throw Error.InvalidIdException("No ID provided")

    val tempFolder = File("/tmp/" + UUID.randomUUID().toString())
    platform.generateLocales(id, version, tempFolder)

    val zip = compressFolder(tempFolder)
    tempFolder.deleteRecursively()

    return zip
  }

  private suspend fun Platform.generateLocales(id: String, version: Int, outputDirectory: File) {
    val csvFile = downloadCsv(id.toFullImportedFileURL())
    val dictionary = localizationDecoder.decode(csvFile.absolutePath)
    val encoder = platformEncodeInteractorMap[this]?.getValue(version)
    encoder?.let {
      it(outputDirectory, dictionary)
    }
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
