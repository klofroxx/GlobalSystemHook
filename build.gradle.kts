plugins {
    kotlin("jvm") version "2.0.20"
}

group = "com.roselia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("lc.kra.system:system-hook:3.8")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}