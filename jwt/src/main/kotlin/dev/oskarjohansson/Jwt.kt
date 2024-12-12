package dev.oskarjohansson
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Jwt

fun main(args: Array<String>) {
    runApplication<Jwt>(*args)
}