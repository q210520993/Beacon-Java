import com.redstone.beacon.utils.Graph
import com.redstone.beacon.api.plugin.DependencyException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

object GraphTest {

    @Test
    fun testAddNodeAndEdge() {
        val graph = Graph<String>()
        graph.addNode("A")
        graph.addNode("B")
        graph.addEdge("A", "B")

        assertEquals(2, graph.adjacencyList.size)
        assertEquals(1, graph.adjacencyList["A"]?.size)
        assertEquals(0, graph.adjacencyList["B"]?.size)
        assertEquals(listOf("B"), graph.adjacencyList["A"])
    }

    @Test
    fun testTopologicalSort() {
        val graph = Graph<String>()
        graph.addNode("A")
        graph.addNode("B")
        graph.addNode("C")
        graph.addNode("D")
        graph.addEdge("A", "B")
        graph.addEdge("B", "C")
        graph.addEdge("A", "D")

        val sortedNodes = graph.topologicalSort()
        val possibleResults = listOf(
            listOf("A", "D", "B", "C"),
            listOf("A", "B", "D", "C"),
            listOf("A", "B", "C", "D")
        )

        assertTrue(possibleResults.contains(sortedNodes))
    }

    @Test
    fun testCircularDependency() {
        val graph = Graph<String>()
        graph.addNode("A")
        graph.addNode("B")
        graph.addEdge("A", "B")
        graph.addEdge("B", "A")

        assertThrows(DependencyException.CircularDependency::class.java) {
            graph.topologicalSort()
        }
    }

    @Test
    fun testComplexCircularDependency() {
        val graph = Graph<String>()
        graph.addNode("A")
        graph.addNode("B")
        graph.addNode("C")
        graph.addEdge("A", "B")
        graph.addEdge("B", "C")
        graph.addEdge("C", "A")

        val exception = assertThrows(DependencyException.CircularDependency::class.java) {
            graph.topologicalSort()
        }


        assertTrue(exception.message?.contains("A") ?: false)
        assertTrue(exception.message?.contains("B") ?: false)
        assertTrue(exception.message?.contains("C") ?: false)
    }
}