plugins {
    id("java")
    // Kotlin 1.9.22 is perfect for 2023-2025 compatibility
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    // Latest platform plugin
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

group = "de.tum.exam"
version = "1.0.7"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
        releases()
    }
}

dependencies {
    intellijPlatform {
        // TARGET: 2023.1.5 (Ultimate)
        // This supports everything from 2023.1 up to 2025.x
        create("IU", "2023.1.5")

        instrumentationTools()
        jetbrainsRuntime()

        // Essential for compilation
        bundledPlugin("com.intellij.java")
    }
}

java {
    toolchain {
        // Java 17 is the native runtime for 2023.x
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks {
    patchPluginXml {
        // 231 = IntelliJ 2023.1
        sinceBuild.set("231")
        untilBuild.set("261.*")
    }

    buildSearchableOptions {
        enabled = false
    }
}