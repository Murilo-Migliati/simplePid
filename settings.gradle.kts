pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform").version("2.0.21")
        id("com.android.library").version("8.2.0")
        id("com.vanniktech.maven.publish") version "0.34.0"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "controllerPidKmp"