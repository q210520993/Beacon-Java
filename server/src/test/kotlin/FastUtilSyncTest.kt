import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.junit.jupiter.api.Test

class FastUtilSyncTest {

    @Test
    fun object2objectOpenHashMap() {
        val map = Object2ObjectOpenHashMap<String, String>()

        val thread1 = Thread {
            for (i in 0..49) {
                map["key$i"] = "value$i"
            }
        }

        val thread2 = Thread {
            for (i in 50..99) {
                map["key$i"] = "value$i"
            }
        }

        thread1.start()
        thread2.start()

        thread1.join()
        thread2.join()

        assert(map.size != 99)
    }
}