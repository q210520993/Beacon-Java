package com.redstone.beacon.api.plugin

/*
* 插件的类加载源
* */
enum class DependencySource {
    // 依赖的插件
    DEPENDENCIES,
    // 你插件自身的Maven，这与依赖的插件相互隔离
    MAVEN,
    // 插件自身
    PLUGIN,
    // 核心
    APPLICATION;
}