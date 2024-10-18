package dev.oskarjohansson

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CrudApp {

    fun main(args: Array<String>) {
        runApplication<CrudApp>(*args)
    }
}