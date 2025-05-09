package com.redstone.beacon.api.plugin

enum class PluginState {
    // 插件准备阶段，刚刚包装好插件
    CREATED,
    // 插件处于关闭状态
    DISABLE,
    // 所有依赖关系都已处理好，插件的ClassLoader已经准备就绪
    RESOLVED,
    // 插件启动成功
    STARTED,
    // 插件加载失败
    FAILED
}