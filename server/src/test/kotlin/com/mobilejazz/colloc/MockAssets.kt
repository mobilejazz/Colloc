package com.mobilejazz.colloc

import kotlin.random.Random

internal fun randomString(
  length: Int = 10,
  allowedChars: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
): String {
  return (1..length)
    .map { allowedChars.random() }
    .joinToString("")
}

internal fun randomInt(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE) = Random.nextInt(min, max)
