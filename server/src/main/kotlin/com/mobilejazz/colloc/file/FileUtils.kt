package com.mobilejazz.colloc.file

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileUtils {
    fun createFile(directory: String, fileName: String): File {
        // Create directory if not doesn't exist
        createDirectory(directory)
        return File(directory, fileName).apply {
            createNewFile()
        }
    }

    fun createDirectory(directory: String): File =
            File(directory).apply {
                mkdirs()
            }

    fun generateZip(fileToZip: File, zipFile: File) {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { zos ->
            fileToZip.walkTopDown().forEach { file ->
                val zipEntry = file.toZipEntry(fileToZip)
                zos.putNextEntry(zipEntry)
                if (file.isFile) {
                    file.inputStream().copyTo(zos)
                }
            }
        }
    }

    private fun File.toZipEntry(sourceFile: File): ZipEntry {
        val zipFileName = this.absolutePath.removePrefix(sourceFile.absolutePath).removePrefix("/")
        return ZipEntry( "$zipFileName${(if (this.isDirectory) "/" else "" )}")
    }
}