package com.redstone.beacon.api.plugin

class PluginException : RuntimeException {
    constructor(s: String) : super(s)
    constructor(cause: Throwable) : super(cause)
}
