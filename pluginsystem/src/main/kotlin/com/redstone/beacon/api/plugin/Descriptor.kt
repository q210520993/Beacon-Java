package com.redstone.beacon.api.plugin

import com.github.zafarkhaja.semver.Version
import java.net.URL

interface Descriptor {
    val name: String
    val main: String
    val version: Version
    val dependencies: List<Dependency>
    // 你插件的URL，类似于File.toURI.toURL
    val url: URL
    // 你插件的URL，类似从网页获取的URL，还未转化的
    val origin: URL
    // 指不安全加载，即使依赖确实版本不对也会强制加载
    val unsafe: Boolean
}