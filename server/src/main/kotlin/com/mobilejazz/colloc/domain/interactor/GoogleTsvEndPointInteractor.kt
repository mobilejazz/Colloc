package com.mobilejazz.colloc.domain.interactor

class GoogleTsvEndPointInteractor {
    operator fun invoke(link: String?): Int {
        val googleLinkValidated = link?.let { it } ?: "https://docs.google.com/a/mobilejazz.com/spreadsheets/d/1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o/export?format=tsv&id=1FYWbBhV_dtlSVOTrhdO2Bd6e6gMhZ5_1iklL-QrkM2o"

        return 1;
    }
}
