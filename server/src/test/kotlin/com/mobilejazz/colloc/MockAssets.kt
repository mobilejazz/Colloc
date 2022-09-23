package com.mobilejazz.colloc

internal fun randomString(
  length: Int = 10,
  allowedChars: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
): String {
  return (1..length)
    .map { allowedChars.random() }
    .joinToString("")
}
