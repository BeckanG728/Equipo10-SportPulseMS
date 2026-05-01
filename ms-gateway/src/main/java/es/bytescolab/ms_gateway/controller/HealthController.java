package es.bytescolab.ms_gateway.controller;

import es.bytescolab.ms_gateway.dto.HealthResponseDto;
import es.bytescolab.ms_gateway.health.ServicesHealthIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private static final String GATEWAY_STATUS = "UP";

    private final ServicesHealthIndicator servicesHealthIndicator;

    @GetMapping("/health")
    public Mono<ResponseEntity<HealthResponseDto>> health() {
        return Mono.fromCallable(servicesHealthIndicator::health)
                .subscribeOn(Schedulers.boundedElastic())
                .map(this::buildResponse);
    }

    private ResponseEntity<HealthResponseDto> buildResponse(Health health) {
        Map<String, String> services = extractServices(health);
        HealthResponseDto body = new HealthResponseDto(
                GATEWAY_STATUS,
                Instant.now().toString(),
                services
        );
        return ResponseEntity.ok(body);
    }

    private Map<String, String> extractServices(Health health) {
        return health.getDetails().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.valueOf(entry.getValue())
                ));
    }
}
