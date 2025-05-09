package com.redstone.beacon.api.plugin

sealed class DependencyException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    // 可添加公共属性（如依赖名称）
    open val dependencyName: String? = null


    /** 依赖未找到 */
    class NotFound(
        override val dependencyName: String,
        cause: Throwable? = null
    ) : DependencyException(
        message = "Dependency '$dependencyName' not found",
        cause = cause
    )

    /** 依赖版本冲突 */
    class VersionConflict(
        override val dependencyName: String,
        val currentVersion: String,
        val requiredVersion: String,
        val type: VersionCheckType,
        cause: Throwable? = null
    ) : DependencyException(
        message = "Dependency '$dependencyName' version conflict: " +
                "current=$currentVersion, required=$requiredVersion, type: ",
        cause = cause
    )

    /** 循环依赖 */
    class CircularDependency(
        val dependencyChain: List<String>,
        cause: Throwable? = null
    ) : DependencyException(
        message = "Circular dependency detected: ${dependencyChain.joinToString(" -> ")}",
        cause = cause
    )

    /** 其他自定义错误（可选） */
    class Other(
        message: String,
        cause: Throwable? = null
    ) : DependencyException(message, cause)
}