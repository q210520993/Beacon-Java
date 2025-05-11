package com.redstone.beacon.api.plugin

import com.redstone.beacon.utils.Graph
import org.slf4j.LoggerFactory

class DefaultSorter: ISorter {


    private val versionChecker = DefaultVersionChecker()

    companion object {
        private val logger = LoggerFactory.getLogger(DefaultSorter::class.java)
    }

    override fun sort(list: List<Descriptor>): SortResult {
        val descriptorMap = list.associateBy { it.name }
        val graph = Graph<String>().apply {
            list.forEach {
                addNode(it.name)
            }
        }

        val result = SortResult()
        list.forEach { descriptor ->
            descriptor.dependencies.forEach { dependency ->
                if (dependency is Dependency.PluginDependency) {
                    try {
                        handleDependency(descriptor, dependency, descriptorMap, graph, versionChecker, result)
                    }catch (ex: Exception) {
                        logger.error("sort a plugin ${descriptor.name} failed", ex)
                    }
                }
            }
        }

        val successSorted = graph.topologicalSort()
        result.sortedPlugins = successSorted
        return result
    }


    private fun handleDependency(
        current: Descriptor,
        dependency: Dependency.PluginDependency,
        descriptorMap: Map<String, Descriptor>,
        graph: Graph<String>,
        versionChecker: VersionChecker,
        result: SortResult
    ) {
        // 1. 检查依赖是否存在
        val targetDescriptor = descriptorMap[dependency.pluginId]
        if (targetDescriptor == null) {
            if (!dependency.optional) {
                result.wrongDependencies[current.name].add(dependency.pluginId)
                graph.removeNode(current.name) // 移除错误插件的节点
                throw DependencyException.NotFound(dependency.pluginId)
            }
            result.wrongSoftDependencies[current.name].add(dependency.pluginId)
            return // 软依赖不存在，直接跳过
        }

        // 2. 检查版本是否匹配
        val checkResult = versionChecker.check(
            type = dependency.versionLimitType,
            expected = dependency.version.toString(),
            actual = targetDescriptor.version.toString()
        )

        // 3. 处理版本检查结果
        if (!checkResult.isSuccess) {
            if (!dependency.optional) {
                result.wrongVersion[current.name].add(dependency.pluginId)
                graph.removeNode(current.name) // 移除错误插件的节点
                throw DependencyException.VersionConflict(
                    dependency.pluginId,
                    targetDescriptor.version.toString(), dependency.version.toString(), dependency.versionLimitType
                )
            }
            result.wrongVersion[current.name].add(dependency.pluginId)
            return // 软依赖版本不匹配，跳过
        }

        // 4. 添加依赖关系到图
        graph.addEdge(from = targetDescriptor.name, to = current.name)
    }


}