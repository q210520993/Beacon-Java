package benchmark

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.LinkedHashMap


object SynchronizedMapBenchmark {

    @Test
    fun thread1(): Unit {
        val iterations = 1000000 // 设置测试操作次数


        // 非线程安全的 fastutil Map
        val fastutilMap = Object2ObjectLinkedOpenHashMap<String, Int>()


        // 线程安全的 fastutil Map
        val synchronizedFastutilMap = Collections.synchronizedMap(Object2ObjectLinkedOpenHashMap<String, Int>())


        // 测试非线程安全的 Map
        val timeFastutil = benchmark(fastutilMap, iterations)
        println("Non-thread-safe fastutil Map: $timeFastutil ms")


        // 测试线程安全的 Map
        val timeSynchronizedFastutil = benchmark(synchronizedFastutilMap, iterations)
        println("Thread-safe fastutil Map (Collections.synchronizedMap): $timeSynchronizedFastutil ms")
    }
    @Test
    fun thread2(): Unit {
        val iterations = 1000000 // 设置测试操作次数


        // 非线程安全的 fastutil Map
        val fastutilMap = java.util.LinkedHashMap<String, Int>()


        // 线程安全的 fastutil Map
        val synchronizedFastutilMap = Collections.synchronizedMap(LinkedHashMap<String, Int>())


        // 测试非线程安全的 Map
        val timeFastutil = benchmark(fastutilMap, iterations)
        println("Non-thread-safe fastutil Map: $timeFastutil ms")


        // 测试线程安全的 Map
        val timeSynchronizedFastutil = benchmark(synchronizedFastutilMap, iterations)
        println("Thread-safe fastutil Map (Collections.synchronizedMap): $timeSynchronizedFastutil ms")
    }

    private fun benchmark(map: MutableMap<String, Int>, iterations: Int): Long {
        val startTime = System.currentTimeMillis()

        for (i in 0 until iterations) {
            map["Key-$i"] = i // 插入键值对
            map["Key-$i"] // 读取键值对
            map.remove("Key-$i") // 删除键值对
        }

        val endTime = System.currentTimeMillis()
        return endTime - startTime
    }
}