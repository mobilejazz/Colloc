package com.mobilejazz.colloc

import com.mobilejazz.colloc.domain.interactor.GoogleTsvEndPointInteractor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GoogleTsvEndPointInteractorTest {
    @Test
    fun `should class be created correctly`() {
        val result  = (GoogleTsvEndPointInteractor())("slkdkfjs")
        assertEquals(result, 1)
    }
}
