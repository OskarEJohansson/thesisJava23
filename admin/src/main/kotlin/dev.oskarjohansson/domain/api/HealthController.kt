package dev.oskarjohansson.domain.api


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {


    @GetMapping("/health")
    fun healthCheck() = "OK"
}
