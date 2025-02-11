plugins {
    kotlin("jvm")
//    id("org.springframework.boot") version "3.2.2"
//    id("io.spring.dependency-management") version "1.1.4"

    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
//    id("org.jetbrains.kotlinx.kover")
    id("idea")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    runtimeOnly("com.h2database:h2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
