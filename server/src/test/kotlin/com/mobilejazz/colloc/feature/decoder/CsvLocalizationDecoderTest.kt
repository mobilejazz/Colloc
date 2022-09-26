package com.mobilejazz.colloc.feature.decoder

import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.ext.createFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class CsvLocalizationDecoderTest {

  @TempDir
  private val testDir: File = File("src/test/resources/csv_parser/")

  val csvContent =
    "                      ,#        ,English ,#          ,Spanish   \n" +
      "                    ,Comments ,en      ,Char count ,es        \n" +
      "#Generic_1          ,         ,        ,           ,          \n" +
      "ls_generic_accept_1 ,         ,Ok      ,2          ,Aceptar   \n" +
      "ls_generic_cancel_1 ,         ,Cancel  ,6          ,Cancelar  \n" +
      "#Generic_2          ,         ,        ,           ,          \n" +
      "ls_generic_accept_2 ,         ,Ok      ,2          ,Aceptar   \n" +
      "ls_generic_cancel_2 ,         ,Cancel  ,6          ,Cancelar    "

  @Test
  fun `assert csv decoder returns correct decoded localization`() {
    val englishTranslation = mapOf(
      "#Generic_1" to "        ",
      "ls_generic_accept_1" to "Ok      ",
      "ls_generic_cancel_1" to "Cancel  ",
      "#Generic_2" to "        ",
      "ls_generic_accept_2" to "Ok      ",
      "ls_generic_cancel_2" to "Cancel  "
    )
    val spanishTranslation = mapOf(
      "#Generic_1" to "          ",
      "ls_generic_accept_1" to "Aceptar   ",
      "ls_generic_cancel_1" to "Cancelar  ",
      "#Generic_2" to "          ",
      "ls_generic_accept_2" to "Aceptar   ",
      "ls_generic_cancel_2" to "Cancelar    "
    )
    val expectedDictionary = mapOf(
      Language("en", "English") to englishTranslation,
      Language("es", "Spanish") to spanishTranslation
    )
    val csvFile = File(testDir, "localizations.csv")
      .createFile()
      .apply {
        writeText(csvContent)
      }

    val actualDictionary = CsvLocalizationDecoder().decode(csvFile.absolutePath)

    assertEquals(expectedDictionary, actualDictionary)
  }
}
