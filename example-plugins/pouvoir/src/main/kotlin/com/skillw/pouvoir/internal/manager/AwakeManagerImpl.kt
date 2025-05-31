package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.api.manager.sub.AwakeManager
import com.skillw.pouvoir.internal.core.awake.AwakeType
import com.skillw.pouvoir.utils.safe
import java.util.Collections

object AwakeManagerImpl: AwakeManager() {
    override val priority: Int = 1
    override val key: String = "AwakeManager"

    init {
        AwakeType.entries.forEach {
            AwakeManagerImpl.putIfAbsent(it, Collections.synchronizedList(mutableListOf()))
        }
    }

    override fun onLoad() {
        get(AwakeType.BeforeServerInit)?.filter {
            !it.isExec
        }?.forEach {
            safe(it::exec)
        }
    }

    override fun onEnable() {
        get(AwakeType.AfterServerInit)?.filter {
            !it.isExec
        }?.forEach {
            safe(it::exec)
        }
    }

    override fun onActive() {
        get(AwakeType.OnServerOpened)?.filter {
            !it.isExec
        }?.forEach {
            safe(it::exec)
        }
    }

    override fun onDisable() {
        get(AwakeType.OnServerClosing)?.filter {
            !it.isExec
        }?.forEach {
            safe(it::exec)
        }
    }

}