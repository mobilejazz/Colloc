package com.mobilejazz.colloc.file

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.io.File


@SpringBootTest
class CollocApplicationTests {

  @Test
  fun `test if file is generated`() {
    val directory = "src/test/resources/file_directory"
    val fileName = "file_name.txt"
    val file = File(directory, fileName)
    FileUtils.createFile(directory, fileName)

    assertTrue(file.exists())
  }

  @Test
  fun `test if directory is generated`() {
    val directory = "src/test/resources/directory"
    val file = File(directory)
    FileUtils.createDirectory(directory)

    assertTrue(file.isDirectory)
  }

  @Test
  fun `test if folder zipped correctly`() {
    val directoryToZip = "src/test/resources/file_directory_to_zip"
    val fileNameToZip_1 = "file_name_for_zip_1.txt"
    val fileNameToZip_2 = "file_name_for_zip_2.txt"
    val fileToZip = File(directoryToZip)

    val zipDirectory = "src/test/resources/zipped_file_directory"
    val zipFileName = "zipped_file.zip"
    val zippedFile = File(zipDirectory, zipFileName)
    FileUtils.createFile(directoryToZip, fileNameToZip_1)
    FileUtils.createFile(zipDirectory, zipFileName)
    File(directoryToZip, fileNameToZip_1).printWriter().use { out ->
      out.println("Some text")
    }
    File(directoryToZip, fileNameToZip_2).printWriter().use { out ->
      out.println("Some text 2")
    }

    FileUtils.generateZip(fileToZip, zippedFile)
  }
}