package com.mobilejazz.colloc.feature.decoder

import com.mobilejazz.colloc.domain.model.Dictionary

interface LocalizationDecoder {
  fun decode(source: String): Dictionary
}
