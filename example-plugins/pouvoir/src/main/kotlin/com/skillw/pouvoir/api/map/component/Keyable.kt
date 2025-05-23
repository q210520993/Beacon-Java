package com.skillw.pouvoir.api.map.component

/**
 * ClassName : com.skillw.classsystem.api.component.Keyable Created by
 * Glom_ on 2021-03-27 20:54:22 Copyright 2021 user. 
 *
 * 有对应键的类
 */
interface Keyable<out K> {
    /** Key */
    val key: K
}