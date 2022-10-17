package com.mobilejazz.colloc.feature.encoder.domain.interactor.angular

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.feature.encoder.domain.interactor.EncodeInteractor
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.io.File


class AngularEncoderV1Interactor(private val json: Json) : EncodeInteractor() {

  private val serializer = MapSerializer(String.serializer(), String.serializer())

  override fun invoke(outputDirectory: File, dictionary: Dictionary) {
    dictionary.forEach {
      writeContent(content = json.encodeToString(serializer, it.value), outputDirectory, outputFileName = localizationFileName(it.key))
    }
  }

  private fun localizationFileName(language: Language): String = "angular/${language.code}.json"
}
