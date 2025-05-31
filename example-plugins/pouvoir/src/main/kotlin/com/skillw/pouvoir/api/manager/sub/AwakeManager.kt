package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.internal.core.awake.AwakeMethod
import com.skillw.pouvoir.internal.core.awake.AwakeType

abstract class AwakeManager: Manager, BaseMap<AwakeType, MutableList<AwakeMethod>>()