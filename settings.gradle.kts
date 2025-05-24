pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "Beacon-Java"
include("pluginsystem")
include("server")
include("example-plugins")
include("example-plugins:fightsystem")
findProject(":example-plugins:fightsystem")?.name = "fightsystem"
include("api-tinylogger")
include("example-plugins:basicplugin")
findProject(":example-plugins:basicplugin")?.name = "basicplugin"
include("example-plugins:pouvoir")
findProject(":example-plugins:pouvoir")?.name = "pouvoir"
