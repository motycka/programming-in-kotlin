pluginManagement {
    plugins {
        val kotlinVersion = "1.9.20"

        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
        id("org.springframework.boot") version "3.2.2"
        id("io.spring.dependency-management") version "1.1.6"
        id("io.kotest") version "0.4.11"
//        id("org.jetbrains.kotlinx.kover") version "0.6.1"
    }

    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "programming-in-kotlin"
include("kotlin-basics")
include("spring-application")
