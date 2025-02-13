import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//project.ext.apply {
//    set("kotlinVersion", "1.9.21")
//}

plugins {
    kotlin("jvm")
    id("io.kotest")
}

group = "com.motycka.edu"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

buildscript {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.kotest")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation("org.slf4j:slf4j-api:2.0.7")
        implementation("ch.qos.logback:logback-classic:1.4.12")
        implementation("io.github.oshai:kotlin-logging:7.0.3")
//        implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.6.1")
//        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

        testImplementation(platform("io.kotest:kotest-bom:4.6.3"))
        testImplementation("io.kotest:kotest-runner-junit5")
        testImplementation("io.kotest:kotest-assertions-core")
        testImplementation("io.mockk:mockk:1.13.2")
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed")
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}


