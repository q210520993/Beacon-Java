package com.skillw.pouvoir.utils

import java.lang.reflect.Method

fun Method.run(src: Any? = null, vararg args: Any) {
    safe {
        val instance = src ?: this.declaringClass.instance ?: return@safe
        this.invoke(instance, *args)
    }
}