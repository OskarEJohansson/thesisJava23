package dev.oskarjohansson
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JwtApp

fun main(args: Array<String>) {
    runApplication<JwtApp>(*args)
}