import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    kotlin("jvm")
}

group = "com.redstone.beacon"
version = "1.0-Alpha"

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases/")
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven")
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/") {
        content { // This filtering is optional, but recommended
            includeModule("net.minestom", "minestom")
            includeModule("net.minestom", "testing")
        }
    }
}

dependencies {

    api(project(":api-tinylogger"))
    api(project(":pluginsystem"))
    api("org.slf4j:slf4j-api:2.0.16")
    implementation("net.minestom:minestom:2025.07.11-1.21.7")
    implementation("org.jline:jline-reader:3.25.0")
    implementation("org.jline:jline-terminal:3.25.0")
    implementation("org.jline:jline-terminal-jna:3.25.0")
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
    relocate("org.tabooproject.", "com.redstone.libs.tabooproject.")
    relocate("taboolib.", "com.redstone.libs.taboolib.")
    //relocate("kotlin.", "com.redstone.libs.kotlin.")
    relocate("org.google.code.gson.", "com.redstone.libs.gson.")
    //relocate("kotlinx.", "com.redstone.libs.kotlinx.")
}

tasks.test {
    useJUnitPlatform()
}