plugins {
    kotlin("jvm")
    id("idea")
}

//repositories {
//    mavenCentral()
//}

dependencies {
//    implementation("org.slf4j:slf4j-api:2.0.7")
//    implementation("ch.qos.logback:logback-classic:1.4.12")
//    implementation("io.github.oshai:kotlin-logging:7.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

//tasks.test {
//    useJUnitPlatform()
//}
//kotlin {
//    jvmToolchain(21)
//}
