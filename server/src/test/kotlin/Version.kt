import com.github.zafarkhaja.semver.Version
import org.junit.jupiter.api.Test

class Version {
    @Test
    fun testUp() {
        val version1 = Version.parse("1.0.0")
        val version2 = Version.parse("1.0.0")
        val b = version1.isHigherThan(version2)
        assert(!b)
    }

}