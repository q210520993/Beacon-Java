plugins {
    id("java")
}

group = "com.redstone.beacon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases/")
}

dependencies {
    compileOnly(project(":server"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}