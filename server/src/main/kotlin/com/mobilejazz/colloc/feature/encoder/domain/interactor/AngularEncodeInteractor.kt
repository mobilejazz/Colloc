package com.mobilejazz.colloc.feature.encoder.domain.interactor

import com.mobilejazz.colloc.domain.model.Dictionary
import com.mobilejazz.colloc.domain.model.Language
import com.mobilejazz.colloc.domain.model.Translation
import com.mobilejazz.colloc.ext.toJsonElement
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.io.File

internal const val HIERARCHY_DELIMITER = "_"

class AngularEncodeInteractor(private val json: Json) : EncodeInteractor() {

  private val serializer = MapSerializer(String.serializer(), JsonElement.serializer())

  override fun invoke(outputDirectory: File, dictionary: Dictionary) {
    dictionary.forEach {
      writeContent(
        content = json.encodeToString(serializer, it.value.encode()),
        outputDirectory,
        outputFileName = localizationFileName(it.key)
      )
    }
  }

  private fun localizationFileName(language: Language): String = "angular/${language.code}.json"

  private fun Translation.encode(): Map<String, JsonElement> {
    val map = mutableMapOf<String, Any>()

    forEach { translation ->
      if (translation.key.isComment())
        translation.key.encodeCommentLine(map)
      else
        translation.encodeTranslationLine(map)
    }

    return map.mapValues {
      it.value.toJsonElement()
    }
  }

  private fun String.encodeCommentLine(map: MutableMap<String, Any>) {
    val comment =
      substring(1) // Remove leading "#" from comment
        .trim()
        .uppercase()
    map[comment] = comment
  }

  private fun Map.Entry<String, String>.encodeTranslationLine(map: MutableMap<String, Any>) {
    key.toHierarchy().apply {
      var hierarchyMap: MutableMap<Any, Any> = map.asMutableMap()
      forEachIndexed { index, hierarchy ->
        if (index in 0 until lastIndex)
          hierarchyMap = hierarchyMap.getOrPut(hierarchy) { mutableMapOf<Any, Any>() }.asMutableMap()

        if (index == lastIndex)
          hierarchyMap[hierarchy] = value
      }
    }
  }


  private fun String.toHierarchy() =
    // Ignore anything until the first delimiter. From Colloc conventions; translation key will start with "ls_"
    substring(indexOf(HIERARCHY_DELIMITER) + 1)
      .split(HIERARCHY_DELIMITER)

  private fun Any.asMutableMap() = this as MutableMap<Any, Any>
}
