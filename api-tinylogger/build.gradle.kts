group = "com.redstone.beacon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("org.tinylog:tinylog-api:2.7.0")
    api("org.tinylog:tinylog-impl:2.7.0")
    api("org.tinylog:slf4j-tinylog:2.7.0")
}

tasks.test {
    useJUnitPlatform()
}