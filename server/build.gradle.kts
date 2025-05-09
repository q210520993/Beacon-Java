import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URI

plugins {
    id("java")
    kotlin("jvm") version "2.0.0"
}

group = "com.redstone.beacon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases/")
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven")
}

dependencies {

    api(project(":pluginsystem"))
    api("net.minestom:minestom-snapshots:9803f2bfe3")
    api("org.slf4j:slf4j-api:2.0.16")
    implementation("org.jline:jline-reader:3.25.0")
    implementation("org.jline:jline-terminal:3.25.0")
    implementation("org.jline:jline-terminal-jna:3.25.0")
    api(project(":api-tinylogger"))
    implementation("org.fusesource.jansi:jansi:2.4.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<ShadowJar> {
    manifest {
        attributes(
            "Main-Class" to "com.redstone.beacon.RunKt",
            "Multi-Release" to true
        )
    }
    mergeServiceFiles()
    relocate("org.tabooproject.", "com.redstone.taboolib.library.")
    relocate("kotlin.", "com.redstone.libs.kotlin.")
    relocate("org.google.code.gson.", "com.redstone.libs.gson.")
    relocate("kotlinx.", "com.redstone.libs.kotlinx.")
}

tasks.test {
    useJUnitPlatform()
}