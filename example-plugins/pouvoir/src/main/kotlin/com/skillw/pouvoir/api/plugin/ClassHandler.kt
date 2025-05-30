package com.skillw.pouvoir.api.plugin

import com.redstone.libs.tabooproject.reflex.ClassStructure
import java.lang.ref.WeakReference


/**
 * @className ClassHandler
 *
 * @author Glom
 * @date 2022/7/18 12:20 Copyright 2022 user.
 */
abstract class ClassHandler(val priority: Int) : Comparable<ClassHandler> {
    /**
     * Inject
     *
     * @param clazz
     * @param plugin
     */
    abstract fun handle(clazz: ClassStructure)

    override fun compareTo(other: ClassHandler): Int = if (this.priority == other.priority) 0
    else if (this.priority > other.priority) 1
    else -1
}