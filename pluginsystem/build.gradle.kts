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
    //都是为了taboolib的configuration
    testImplementation(kotlin("test"))
    implementation("org.yaml:snakeyaml:2.2")
    implementation("com.typesafe:config:1.4.3")
    implementation("com.electronwill.night-config:core:3.6.7")
    implementation("org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-depchain:3.3.4")
    implementation("com.electronwill.night-config:toml:3.6.7")
    implementation("com.electronwill.night-config:json:3.6.7")
    implementation("com.electronwill.night-config:hocon:3.6.7")
    implementation("com.electronwill.night-config:core-conversion:6.0.0")
    implementation("org.tabooproject.reflex:analyser:1.0.23")
    implementation("org.tabooproject.reflex:fast-instance-getter:1.0.23")
    implementation("com.google.guava:guava:32.0.0-android")
    implementation("org.tabooproject.reflex:reflex:1.0.23") // 需要 analyser 模块
    api("com.google.code.gson:gson:2.11.0")
    api("com.github.zafarkhaja:java-semver:0.10.2")
    compileOnly("it.unimi.dsi:fastutil:8.5.14")
    testImplementation("it.unimi.dsi:fastutil:8.5.14")
    compileOnly("org.slf4j:slf4j-api:2.0.16")
}


tasks.test {
    useJUnitPlatform()
}