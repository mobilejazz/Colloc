package com.mobilejazz.colloc.ext

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.boot.test.context.SpringBootTest
import java.io.File


@SpringBootTest
class CollocApplicationTests {

  @TempDir
  private val testDir: File = File("src/test/resources/file_utils/")

  @Test
  fun `test if file is generated`() {
    val fileName = "$testDir/file_directory/file_name.txt"
    val file = File(fileName).createFile()

    assertTrue(file.exists())
  }

  @Test
  fun `test if folder zipped correctly`() {
    val directoryToZip = "$testDir/file_directory_to_zip"
    val fileNameToZip_1 = "$directoryToZip/file_name_for_zip_1.txt"
    val fileNameToZip_2 = "$directoryToZip/file_name_for_zip_2.txt"
    val fileToZip = File(directoryToZip)

    val zipDirectory = "$testDir/zipped_file_directory"
    val zipFileName = "$zipDirectory/zipped_file.zip"
    val zippedFile = File(zipFileName)
    val fileToZip_1 = File(fileNameToZip_1).createFile()
    val fileToZip_2 = File(fileNameToZip_2).createFile()
    zippedFile.createFile()
    fileToZip_1.printWriter().use { out ->
      out.println("Some text")
    }
    fileToZip_2.printWriter().use { out ->
      out.println("Some text 2")
    }

    fileToZip.generateZip(zippedFile)
  }
}
