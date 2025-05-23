package com.redstone.beacon.utils

import java.lang.reflect.Method
import kotlin.reflect.full.createInstance

internal fun Class<*>.isSingle(): Boolean {
    try {
        this.getDeclaredField("INSTANCE").get(null) != null
        return true
    } catch (e: Exception) {
        return false
    }
}

internal val Class<*>.instance: Any?
    get() = try {
        this.getDeclaredField("INSTANCE").get(null)
    } catch (_: NoSuchFieldException) {
        safe {
            this.kotlin.createInstance()
        }
    }

internal fun Method.run(vararg args: Any) {
    if (this.declaringClass.isSingle()) {
        this.invoke(this.declaringClass.instance, *args)
        return
    }
    this.invoke(null, *args)
}
