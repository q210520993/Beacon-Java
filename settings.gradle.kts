plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "Beacon-Java"
include("pluginsystem")
include("server")
include("example-plugins")
include("example-plugins:fightsystem")
findProject(":example-plugins:fightsystem")?.name = "fightsystem"
include("example-plugins:core")
findProject(":example-plugins:core")?.name = "core"
include("api-tinylogger")
include("example-plugins:basicplugin")
findProject(":example-plugins:basicplugin")?.name = "basicplugin"
