package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class GoogleTsvEndPointInteractorTest {
    @Test
    fun `no link provided returns null`() {
        val result = (GoogleTsvEndPointInteractor())("")
        assertEquals(result, null)
    }

    @Test
    fun `correct link generates a file`() {
        val link = "https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o"
        val result = (GoogleTsvEndPointInteractor())(link)
        assert(result is File)
    }
}
