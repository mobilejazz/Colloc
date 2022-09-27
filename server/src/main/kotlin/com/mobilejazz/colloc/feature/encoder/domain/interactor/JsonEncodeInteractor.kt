package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Dictionary
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.io.File

class JsonEncodeInteractor(private val json: Json): EncodeInteractor() {

  private val serializer = MapSerializer(String.serializer(), MapSerializer(String.serializer(), String.serializer()))

  override fun invoke(outputDirectory: File, dictionary: Dictionary) {
    writeContent(
      content = dictionary.encodeContent(),
      outputDirectory,
      outputFileName = "stringsFromApp.json"
    )
  }

  private fun Dictionary.encodeContent() = json.encodeToString(
    serializer,
    mapValues {
      it.value.toMutableMap().apply {
        keys.removeAll { key -> key.isComment() }
      }
    }.mapKeys { it.key.code }
  )
}
