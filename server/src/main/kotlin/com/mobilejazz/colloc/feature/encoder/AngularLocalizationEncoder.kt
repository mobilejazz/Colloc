package com.mobilejazz.colloc.feature.encoder

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.domain.model.Language
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class AngularLocalizationEncoder(private val json: Json) : LocalizationEncoder {

  private val serializer = MapSerializer(String.serializer(), String.serializer())

  override fun encodeContent(dictionary: Dictionary): Map<Language, String> =
    dictionary.mapValues {
      json.encodeToString(serializer, it.value)
    }

  override fun encodeLocalizationFileName(language: Language): String = "angular/${language.name}.json"
}
