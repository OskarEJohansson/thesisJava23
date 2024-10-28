import org.jetbrains.kotlin.gradle.dsl.JvmTarget

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions{ jvmTarget.set(JvmTarget.JVM_21)}
}

dependencies{
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security:spring-security-core:6.3.3")
    implementation("org.springframework.security:spring-security-oauth2-jose:6.3.3")
    implementation("io.ktor:ktor-client-core:3.0.0")
    implementation("io.ktor:ktor-client-cio-jvm:3.0.0")
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")
}