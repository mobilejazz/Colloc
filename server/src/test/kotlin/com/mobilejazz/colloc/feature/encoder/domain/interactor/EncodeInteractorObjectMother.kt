package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Translation
import com.mobilejazz.colloc.randomInt
import com.mobilejazz.colloc.randomString

internal fun anyTranslation(): Translation = buildMap {
  repeat(randomInt(min = 1, max = 20)) {
    put(randomString(), randomString())
  }
}
