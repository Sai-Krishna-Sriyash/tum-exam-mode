plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

group = "de.tum.exam"
// UPDATED: Version 1.0.6
version = "1.0.6"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        // We build against 2023.1.5 (Ultimate).
        // This makes the plugin compatible with 2023.1, 2023.2, 2023.3, 2024.x, 2025.x
        val type = providers.gradleProperty("platformType").getOrElse("IU")
        create(type, "2023.1.5")

        instrumentationTools()
        jetbrainsRuntime()
    }
}

java {
    toolchain {
        // Java 17 is the standard for 2022+ IDEs.
        // It works perfectly on the newest 2025 versions too.
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks {
    patchPluginXml {
        // "231" corresponds to 2023.1
        sinceBuild.set("231")
        // "261.*" keeps it open for future versions
        untilBuild.set("261.*")
    }

    buildSearchableOptions {
        enabled = false
    }
}