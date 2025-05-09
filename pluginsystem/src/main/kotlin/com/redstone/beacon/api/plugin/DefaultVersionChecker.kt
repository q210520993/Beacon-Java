package com.redstone.beacon.api.plugin

import com.github.zafarkhaja.semver.Version

class DefaultVersionChecker: VersionChecker {

    override fun check(type: VersionCheckType, expected: String, actual: String): VersionCheckResult {
        val expectedVersion = Version.parse(expected)
        val actualVersion = Version.parse(actual)

        val success = when(type) {
            VersionCheckType.UP -> actualVersion.isHigherThan(expectedVersion)
            VersionCheckType.DOWN -> actualVersion.isLowerThan(expectedVersion)
            else -> actualVersion.isHigherThan(expectedVersion)
        }

        return VersionCheckResult(success, expected, actual, type)
    }

}