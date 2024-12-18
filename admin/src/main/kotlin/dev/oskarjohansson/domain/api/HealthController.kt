package dev.oskarjohansson.domain.api


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    private var LOG: Logger = LoggerFactory.getLogger(HealthController::class.java)

    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<String> {
        LOG.info("Health check complete")
        return ResponseEntity.ok("Health check complete")
    }
}
