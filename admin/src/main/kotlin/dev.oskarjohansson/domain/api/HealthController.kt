package dev.oskarjohansson.domain.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {


    @GetMapping("admin/health")
    fun healthCheck():ResponseEntity<String>{

        return ResponseEntity.status(HttpStatus.OK).build()
    }
}