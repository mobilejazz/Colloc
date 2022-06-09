package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class GoogleTsvEndPointInteractorTest {
    @Test
    fun `no link returns null`() {
        val result = (GoogleTsvEndPointInteractor())("")
        assertEquals(result, null)
    }

    @Test
    fun `incorrect link returns null`() {
        val result = (GoogleTsvEndPointInteractor())("some random string")
        assertEquals(result, null)
    }

    @Test
    fun `no google docs link returns null`() {
        val result = (GoogleTsvEndPointInteractor())("https://www.google.com")
        assertEquals(result, null)
    }

    @Test
    fun `no google spreadsheets link returns null`() {
        val result = (GoogleTsvEndPointInteractor())("https://docs.google.com/a/somerandomstring")
        assertEquals(result, null)
    }

    @Test
    fun `correct link generates a file`() {
        val link = "https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o"
        val result = (GoogleTsvEndPointInteractor())(link)
        assert(result is File)
    }
}
