rootProject.name = "tum-exam-mode"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // This plugin allows Gradle to auto-download Java 11 (or any version)
    // This fixes the "Cannot find a Java installation" error.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}