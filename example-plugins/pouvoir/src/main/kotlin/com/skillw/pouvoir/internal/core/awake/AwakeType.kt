package com.skillw.pouvoir.internal.core.awake

/**
 * 表示服务器生命周期事件不同阶段的枚举类
 * Enum class representing the different stages of server lifecycle events.
 */
enum class AwakeType {
    /**
     * 表示服务器初始化之前的阶段
     * 可在此阶段使用 mixins 修改服务器设置或配置
     * Represents the stage before the server initialization.
     * This stage allows for modifications using mixins to alter server settings
     * or configurations prior to the server being fully set up.
     */
    BeforeServerInit,

    /**
     * 表示服务器已初始化但尚未打开的阶段
     * 可在此阶段执行需要服务器初始化但尚未接受连接的额外设置任务
     * Represents the stage after the server has been initialized but before it is opened.
     * This stage can be used to perform additional setup tasks that require the server
     * to be initialized but not yet accepting connections.
     */
    AfterServerInit,

    /**
     * 表示服务器已打开并正在接受连接的阶段
     * 可在此阶段触发服务器上线后需要进行的操作
     * Represents the stage when the server has been opened and is now accepting connections.
     * Use this stage to trigger actions that need to occur once the server is live.
     */
    OnServerOpened,

    /**
     * 表示服务器正在关闭的阶段
     * 可在此阶段执行清理任务或在服务器关闭前保存状态
     * Represents the stage when the server is in the process of closing.
     * This stage can be used to perform cleanup tasks or save states before the server shuts down.
     */
    OnServerClosing,
}