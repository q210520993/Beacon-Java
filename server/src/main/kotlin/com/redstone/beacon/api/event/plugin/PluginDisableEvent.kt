package com.redstone.beacon.api.event.plugin

import com.redstone.beacon.api.plugin.PluginWrapper
import net.minestom.server.event.trait.CancellableEvent

class PluginDisableEvent {

    class Pre(override val plugin: PluginWrapper) : PluginEvent, CancellableEvent {
        private var canceled_: Boolean = false
        override fun setCancelled(p0: Boolean) {
            canceled_ = p0
        }
        override fun isCancelled(): Boolean {
            return canceled_
        }
    }

    class Post(override val plugin: PluginWrapper) : PluginEvent

}