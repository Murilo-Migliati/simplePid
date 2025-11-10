group = "io.github.murilo-migliati"
version = "1.0"

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin")
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

            artifactId = "simplepidkmm"

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

}


signing {
    val gpgKey = System.getenv("GPG_SIGNING_KEY_ARMORED")
    val gpgPassword = System.getenv("GPG_SIGNING_PASSWORD")

    useInMemoryPgpKeys(gpgKey, gpgPassword)
    sign(publishing.publications)
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            username.set(System.getenv("SONATYPE_USERNAME"))
            password.set(System.getenv("SONATYPE_TOKEN"))
        }
    }
}