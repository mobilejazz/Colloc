package com.mobilejazz.colloc.csv

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ParseCsvInteractorTest {

    @Test
    fun `test if all languages are listed`() {
        ParseCsvInteractor()("src/test/resources/colloc.csv")
    }
}