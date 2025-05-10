import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    kotlin("jvm") version "2.0.0"
}

group = "com.redstone.beacon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly(project(":server"))
    compileOnly("dev.hollowcube:polar:1.14.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<ShadowJar> {
    relocate("kotlin.", "com.skillw.basicplugin.kotlin.")
}