pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform").version("2.0.21")
        id("com.android.library").version("8.2.0")
        id("io.github.gradle-nexus.publish-plugin").version("2.0.0")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "simplePidKmm"