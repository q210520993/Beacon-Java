package com.redstone.beacon.utils

import com.redstone.beacon.api.plugin.DependencyException


/**
 * @author Clok
 * @Date 2025/2/22 15:24
*/
class Graph<T : Any> {
    // 使用 LinkedHashMap 保持插入顺序
    val adjacencyList = linkedMapOf<T, MutableList<T>>()
    private val inDegree = linkedMapOf<T, Int>()

    /**
     * 添加节点
     * @param node 图节点（插件描述）
     */
    fun addNode(node: T) {
        adjacencyList.getOrPut(node) { mutableListOf() }
        inDegree.getOrPut(node) { 0 }
    }

    /**
     * 添加有向边
     * @param from 边的起点（被依赖方）
     * @param to 边的终点（依赖方）
     */
    fun addEdge(from: T, to: T) {
        adjacencyList.getOrPut(from) { mutableListOf() }.add(to)
        inDegree[to] = inDegree.getOrDefault(to, 0) + 1
    }

    fun topologicalSort(): List<T> {
        val queue = ArrayDeque<T>().apply {
            addAll(inDegree.filter { it.value == 0 }.keys)
        }
        val result = mutableListOf<T>()
        val workingInDegree = inDegree.toMutableMap()

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            result.add(node)

            adjacencyList[node]?.forEach { neighbor ->
                workingInDegree[neighbor] = workingInDegree[neighbor]!! - 1
                if (workingInDegree[neighbor] == 0) {
                    queue.add(neighbor)
                }
            }
        }

        if (result.size != adjacencyList.size) {
            val cyclicNodes = adjacencyList.keys - result.toSet()
            val cyclePath = findCyclePath(cyclicNodes.firstOrNull())
                ?: throw IllegalStateException()
            throw DependencyException.CircularDependency(cyclePath)
        }

        return result
    }

    // dfs
    private fun findCyclePath(startNode: T?): List<String>? {
        if (startNode == null) return null

        val visited = mutableSetOf<T>()
        val path = mutableListOf<T>()
        val stack = mutableListOf<T>()

        fun dfs(current: T): Boolean {
            if (current in stack) {
                val cycleStart = stack.indexOf(current)
                path.addAll(stack.subList(cycleStart, stack.size))
                path.add(current)
                return true
            }
            if (current in visited) return false

            visited.add(current)
            stack.add(current)

            adjacencyList[current]?.forEach { neighbor ->
                if (dfs(neighbor)) return true
            }

            stack.removeAt(stack.size - 1)
            return false
        }

        return if (dfs(startNode)) path.map { it.toString() } else null
    }


}