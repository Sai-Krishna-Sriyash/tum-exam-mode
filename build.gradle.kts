plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

group = "de.tum.exam"
// FINAL VERSION: 1.0.4
version = "1.0.4"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        val type = providers.gradleProperty("platformType").getOrElse("IU")
        create(type, "2025.2.5")
        instrumentationTools()
        jetbrainsRuntime()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    patchPluginXml {
        sinceBuild.set("252")
        untilBuild.set("261.*")
    }

    buildSearchableOptions {
        enabled = false
    }
}