package es.bytescolab.ms_gateway.controller;

import es.bytescolab.ms_gateway.exception.domain.ServiceUnavailableException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/{service}")
    public Mono<Void> fallback(@PathVariable String service) {
        return Mono.error(new ServiceUnavailableException(service));
    }
}
