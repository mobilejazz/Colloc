package com.mobilejazz.colloc.ext

import java.net.URL

fun String.toFullImportedFileURL() = URL("https://docs.google.com/spreadsheets/d/${this}/export?format=csv")

private val questionMarkAtStartRegex by lazy { "^(\\?)".toRegex() }
private val referenceAtStartRegex by lazy { "^(@)".toRegex() }
private val tagOpenRegex by lazy { "^(<)".toRegex() }
private val tagCloseRegex by lazy { "^(>)".toRegex() }
internal fun String.encodeAndroidLiterals(): String =
  replace("%@", "%s")
    .replace(referenceAtStartRegex, "\\\\@")
    .replace(questionMarkAtStartRegex, "\\\\?")
    .replace("'", "\\'")
    .replace("&", "&amp;")
    .replace(tagOpenRegex, "&lt;")
    .replace(tagCloseRegex, "&gt;")
    .replace("\"", "\\\"")
