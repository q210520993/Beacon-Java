package com.redstone.beacon.api.plugin

interface VersionChecker {
    // 查询类型 预期版本 实际版本
    fun check(type: VersionCheckType, expected: String, actual: String): VersionCheckResult
}

data class VersionCheckResult(val isSuccess: Boolean, val expected: String, val actual: String, val type: VersionCheckType)

enum class VersionCheckType {
    UP,
    DOWN
}