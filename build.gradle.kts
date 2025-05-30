plugins {
    id("java")
    id("java-library")
    id("io.github.goooler.shadow") version ("8.1.2")
}

group = project(":server").group
version = project(":server").version

subprojects {
    plugins.apply("io.github.goooler.shadow")
    plugins.apply("java-library")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://www.jitpack.io/")
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.36")
        annotationProcessor("org.projectlombok:lombok:1.18.36")
        testCompileOnly("org.projectlombok:lombok:1.18.36")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
    }
    tasks {
        withType<Copy> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.compilerArgs.add("-Xlint:deprecation")
            configureEach {
                options.isFork = true
            }
        }

        test {
            useJUnitPlatform()
        }
    }
}
