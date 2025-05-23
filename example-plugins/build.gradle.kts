plugins {
    id("java")
}

group = "com.redstone.beacon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly(project(":server"))
}

tasks.test {
    useJUnitPlatform()
}