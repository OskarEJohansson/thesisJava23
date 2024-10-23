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
}