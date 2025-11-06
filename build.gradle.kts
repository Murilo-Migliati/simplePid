group = "io.github.murilo-migliati"
version = "1.0"

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.murilomigliati.pid"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    androidTarget()
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvmToolchain(17)

    sourceSets {
        commonMain.dependencies {
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

publishing {
    publications {
        named<MavenPublication>("kotlinMultiplatform") {
            pom {
                name = "Simple PID KMM"
                description = "A lightweight, dependency-free PID controller for Kotlin Multiplatform."
                url = "https://github.com/Murilo-Migliati/simplePid"
                licenses {
                    license {
                        name = "The MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                developers {
                    developer {
                        id = "Murilo-Migliati"
                        name = "Murilo Migliati"
                    }
                }
                scm {
                    connection = "scm:git:github.com/Murilo-Migliati/simplePid.git"
                    url = "https://github.com/Murilo-Migliati/simplePid"
                }
            }
        }
    }

    repositories {
        maven {
            name = "CentralPortal"
            url = uri("https://central.sonatype.com/api/v1/publisher")
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_TOKEN")
            }
        }
    }
}

signing {
    val gpgPassword = System.getenv("GPG_SIGNING_PASSWORD")
    val gpgKeyArmored = System.getenv("GPG_SIGNING_KEY_ARMORED")

    useInMemoryPgpKeys(gpgKeyArmored, gpgPassword)
    sign(publishing.publications)
}