package com.mobilejazz.colloc.feature.encoder

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.domain.model.Language

interface LocalizationEncoder {

  fun encodeContent(dictionary: Dictionary): Map<Language, String>

  fun encodeLocalizationFileName(language: Language): String

}
