import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    kotlin("jvm")
}

group = "com.redstone.beacon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases/")
}
dependencies {
    api("com.google.code.gson:gson:2.11.0")
    api("com.github.zafarkhaja:java-semver:0.10.2")
    // taboolib
    //都是为了taboolib的configuration
    implementation("org.ow2.asm:asm:9.7.1")
    implementation("org.ow2.asm:asm-util:9.7.1")
    implementation("org.ow2.asm:asm-commons:9.7.1")
    implementation("org.apache.commons:commons-lang3:3.5")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("com.typesafe:config:1.4.3")
    implementation("com.electronwill.night-config:core:3.6.7")
    implementation("org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-depchain:3.3.4")
    implementation("com.electronwill.night-config:toml:3.6.7")
    implementation("com.electronwill.night-config:json:3.6.7")
    implementation("com.electronwill.night-config:hocon:3.6.7")
    implementation("com.electronwill.night-config:core-conversion:6.0.0")
    implementation("com.google.guava:guava:32.0.0-android")
    compileOnly("it.unimi.dsi:fastutil:8.5.14")
    compileOnly("org.slf4j:slf4j-api:2.0.16")

    testImplementation("it.unimi.dsi:fastutil:8.5.14")
    testImplementation("org.apache.commons:commons-lang3:3.5")
    testImplementation(kotlin("test"))
}


tasks.test {
    useJUnitPlatform()
}