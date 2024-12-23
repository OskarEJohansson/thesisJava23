import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false
    id("org.springframework.boot") version "3.3.4" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
}

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

    if(project.name == "commons"){
        tasks.withType<BootJar> { enabled = false }

        tasks.withType<Jar> {
            archiveBaseName.set("commons")
            archiveVersion.set("1.0")
        }
    }
}
