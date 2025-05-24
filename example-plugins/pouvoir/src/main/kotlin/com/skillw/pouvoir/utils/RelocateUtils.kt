package com.skillw.pouvoir.utils

import java.util.concurrent.ConcurrentHashMap

private val relocates by lazy {
    return@lazy ConcurrentHashMap<String, String>()
}

fun relocate(from: String, to: String) {
    relocates[from] = to
}

fun deleteRelocate(from: String) {
    relocates.remove(from)
}

fun String.relocatePath(): String {
    return this.replacement(relocates)
}