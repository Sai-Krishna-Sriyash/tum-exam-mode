plugins {
    id("java")
    // Kotlin 2.1.0 is required for IntelliJ 2025.2+
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    // UPGRADE: Using the latest stable version to fix the "Index: 1" crash
    id("org.jetbrains.intellij.platform") version "2.10.5"
}

group = "de.tum.exam"
version = "1.0.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        // We build against the latest Ultimate 2025.2.5
        val type = providers.gradleProperty("platformType").getOrElse("IU")
        create(type, "2025.2.5")

        // REQUIRED: Helper tool for code instrumentation
        instrumentationTools()

        // REQUIRED: Explicitly request the JetBrains Runtime to avoid launch crashes
        jetbrainsRuntime()
    }
}

// MODERN CONFIGURATION: Use Gradle Toolchains to guarantee Java 21
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
}