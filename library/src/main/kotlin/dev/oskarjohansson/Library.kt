package dev.oskarjohansson

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Library

fun main(args: Array<String>) {
    runApplication<Library>(*args)
}