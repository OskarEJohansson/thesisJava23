plugins {
    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false
    id("org.springframework.boot") version "3.3.4" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
}

group = "com.OskarJohansson"
version = "0.0.1-SNAPSHOT"

allprojects{
    repositories {
        mavenCentral()
    }
}

subprojects {

    apply{
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}