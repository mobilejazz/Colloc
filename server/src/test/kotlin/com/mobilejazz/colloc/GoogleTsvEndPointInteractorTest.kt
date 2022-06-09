package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import com.mobilejazz.colloc.domain.model.Platform
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class GoogleTsvEndPointInteractorTest {
    @Test
    fun `no link returns null`() {
        runBlocking {
            assertThrows<GoogleTsvEndPointInteractor.Error.InvalidURLException> {
                (GoogleTsvEndPointInteractor())("", listOf(Platform.IOS))
            }
        }
    }

    @Test
    fun `incorrect link returns null`() {
        runBlocking {
            assertThrows<GoogleTsvEndPointInteractor.Error.InvalidURLException> {
                (GoogleTsvEndPointInteractor())("some random string", listOf(Platform.IOS))
            }
        }
    }

    @Test
    fun `no google docs link returns null`() {
        runBlocking {
            assertThrows<GoogleTsvEndPointInteractor.Error.InvalidURLException> {
                (GoogleTsvEndPointInteractor())("https://www.google.com", listOf(Platform.IOS))
            }
        }
    }

    @Test
    fun `no google spreadsheets link returns null`() {
        runBlocking {
            assertThrows<GoogleTsvEndPointInteractor.Error.InvalidURLException> {
                (GoogleTsvEndPointInteractor())("https://docs.google.com/a/somerandomstring", listOf(Platform.IOS))
            }
        }
    }

    @Test
    fun `correct link generates a file`() {
        runBlocking {
            val link =
                "https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o"
            val result = (GoogleTsvEndPointInteractor())(link, listOf(Platform.IOS, Platform.ANDROID, Platform.JSON))
            println(result?.absolutePath)
            assert(result is File)
        }
    }
}
