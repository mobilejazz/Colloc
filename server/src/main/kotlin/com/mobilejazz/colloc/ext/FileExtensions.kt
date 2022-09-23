package com.mobilejazz.colloc.ext

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun File.createFile(): File {
  parentFile?.mkdirs()
  createNewFile()
  return this
}

fun File.generateZip(zipFile: File) {
  ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { zos ->
    walkTopDown().forEach { file ->
      val zipEntry = file.toZipEntry(this)
      zos.putNextEntry(zipEntry)
      if (file.isFile) {
        file.inputStream().copyTo(zos)
      }
    }
  }
}

private fun File.toZipEntry(sourceFile: File): ZipEntry {
  val zipFileName = this.absolutePath.removePrefix(sourceFile.absolutePath).removePrefix("/")
  return ZipEntry("$zipFileName${(if (this.isDirectory) "/" else "")}")
}
