pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "java-autopilot"
include("jatp-core")
include("jatp-analyzer")
include("jatp-inspector")
include("jatp-automation")
include("jatp-ai")
include("jatp-api")
