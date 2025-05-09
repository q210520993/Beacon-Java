package com.redstone.beacon.utils

/**
 * @className Safe
 *
 * @author Glom
 * @date 2023/1/7 22:18 Copyright 2024 Glom.
 */

fun <T> safe(run: () -> T): T? {
    return runCatching { run() }.run {
        if (isSuccess) getOrNull()
        else {
            exceptionOrNull()?.printStackTrace()
            null
        }
    }
}