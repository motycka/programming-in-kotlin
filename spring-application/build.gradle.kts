plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("idea")
    id("nu.studer.jooq")
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:2.1.10")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    // implementation("org.springframework.boot:spring-boot-starter-webflux:3.4.2")
    // runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")

    // jOOQ code generation (Optional, if you want to generate schema classes)
    jooqGenerator("org.jooq:jooq-meta")
    jooqGenerator("org.jooq:jooq-codegen")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "mockito-core")
    }
    testImplementation("io.mockk:mockk:1.13.16")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
}


tasks.test {
    useJUnitPlatform()
}

//jooq {
//    configurations {
//        create("main") {
//            generationTool {
//                jdbc {
//                    driver = "org.h2.Driver"
//                    url = "jdbc:h2:mem:fantasyspace;DB_CLOSE_DELAY=-1"
//                    user = "sa"
//                    password = ""
//                }
//                generator {
//                    name = "org.jooq.codegen.DefaultGenerator"
//                    database {
//                        inputSchema = "PUBLIC"
//                        includes = ".*" // Generates classes for all tables
//                    }
//                    target {
//                        packageName = "com.motycka.edu"  // Change as needed
//                        directory = "src/main/generated"  // Path to generated sources
//                    }
//                }
//            }
//        }
//    }
//}
