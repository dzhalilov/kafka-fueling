import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

var githubUser: String by extra { System.getProperty("gradle.githubUser") }
var githubToken: String by extra { System.getProperty("gradle.githubToken") }

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/dzhalilov/lib-kafka-fueling")
        credentials {
            username = githubUser
            password = githubToken
        }
    }
//       maven { url = uri("https://jitpack.io") }
}

dependencies {
//    implementation("com.github.dzhalilov:lib-kafka-fueling:1.0")
//    implementation("com.github.dzhalilov:lib-kafka-fueling:1.8-SNAPSHOT")
    implementation("com.github.dzhalilov:lib-kafka-fueling:2.2-SNAPSHOT")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
