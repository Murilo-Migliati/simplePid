plugins {
    kotlin("jvm") version "2.0.21"
    //id("java-library")
}

group = "com"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}