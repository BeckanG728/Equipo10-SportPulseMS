package es.bytescolab.ms_gateway.exception.domain;

public class RateLimitException extends GatewayDomainException {

    public RateLimitException(int retryAfter) {
        super("RATE_LIMIT_EXCEEDED", "Demasiadas peticiones. Límite: 60 req/min");
        withMetadata("retryAfter", retryAfter);
    }
}
