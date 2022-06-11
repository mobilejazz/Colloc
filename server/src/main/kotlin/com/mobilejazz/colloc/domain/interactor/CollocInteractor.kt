package com.mobilejazz.colloc.domain.interactor

import com.mobilejazz.colloc.csv.ParseCsvInteractor
import com.mobilejazz.colloc.domain.model.Platform
import com.mobilejazz.colloc.file.FileUtils
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.io.File
import java.net.URL
import java.util.*

@Service
class CollocInteractor(
    private val downloadFileInteractor: DownloadFileInteractor,
    private val collocClassicInteractor: CollocClassicInteractor,
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
                Platform.ANGULAR -> {
                    val url = URL("https://docs.google.com/spreadsheets/d/${id}/export?format=csv")
                    val csv = downloadCsv(url)
                    generateAngularLocales(csv, tempFolder)
                }
                else -> {
                    collocClassicInteractor(id, tempFolder, platform)
                }
            }
        }

        val zip = compressFolder(tempFolder)
        tempFolder.deleteRecursively()

        return zip
    }

    private suspend fun downloadCsv(link: URL): File = downloadFileInteractor(link, "downloaded_google_drive_file.csv")

    private fun generateAngularLocales(csvFile: File, output: File) {
        val parse = ParseCsvInteractor()
        val result = parse(csvFile.absolutePath)
        val json = Json { }
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
