package com.mobilejazz.colloc

import com.mobilejazz.colloc.classic.Platform
import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class GoogleTsvEndPointInteractorTest {
    @Test
    suspend fun `no link returns null`() {
        val result = (GoogleTsvEndPointInteractor())("", listOf(Platform.IOS))
        assertEquals(result, null)
    }

    @Test
    suspend fun `incorrect link returns null`() {
        val result = (GoogleTsvEndPointInteractor())("some random string", listOf(Platform.IOS))
        assertEquals(result, null)
    }

    @Test
    suspend fun `no google docs link returns null`() {
        val result = (GoogleTsvEndPointInteractor())("https://www.google.com", listOf(Platform.IOS))
        assertEquals(result, null)
    }

    @Test
    suspend fun `no google spreadsheets link returns null`() {
        val result = (GoogleTsvEndPointInteractor())("https://docs.google.com/a/somerandomstring", listOf(Platform.IOS))
        assertEquals(result, null)
    }

    @Test
    fun `correct link generates a file`() {
        runBlocking {
            val link =
                "https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o"
            val result = (GoogleTsvEndPointInteractor())(link, listOf(Platform.IOS,Platform.ANDROID, Platform.JSON))
            println(result?.absolutePath)
            assert(result is File)
        }
    }
}
