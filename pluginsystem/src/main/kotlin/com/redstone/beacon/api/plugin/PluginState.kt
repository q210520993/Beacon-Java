package com.redstone.beacon.api.plugin

enum class PluginState(val level: Int) {
    // 插件准备阶段，刚刚包装好插件
    CREATED(1),
    // 插件处于关闭状态
    DISABLE(-1),
    // 所有依赖关系都已处理好，插件的ClassLoader已经准备就绪
    RESOLVED(0),
    // 插件启动成功
    STARTED(2),
    // 服务器启动之后激活插件的部分
    ACTIVED(3),
    // 插件加载失败
    FAILED(-2)
}