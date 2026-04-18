package es.bytescolab.ms_gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/{service}")
    public Mono<ResponseEntity<Map<String, Object>>> fallback(
            @PathVariable String service) {

        Map<String, Object> body = Map.of(
                "code", "SERVICE_UNAVAILABLE",
                "service", service,
                "message", "Service is down",
                "timestamp", Instant.now().toString()
        );

        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(body)
        );
    }
}
