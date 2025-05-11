package com.redstone.beacon.api.plugin

interface ISorter {

    fun sort(list: List<Descriptor>): SortResult

}

class SortResult {
    // 这个是强依赖缺失列表 插件名称 -> 依赖信息
    val wrongDependencies = AutoHashMap()
    // 这个是版本依赖缺失列表 插件名称 -> 依赖信息
    val wrongVersion = AutoHashMap()
    // 这个是版本依赖缺失列表 插件名称 -> 依赖信息
    val wrongSoftDependencies = AutoHashMap()
    // 处理后的
    lateinit var sortedPlugins: List<String>

    inner class AutoHashMap : HashMap<String, MutableList<String>>() {
        override fun get(key: String): MutableList<String> {
            val a = super.get(key) ?: return put(key, mutableListOf()) ?: mutableListOf()
            return a
        }
    }

}