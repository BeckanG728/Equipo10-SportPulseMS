package es.bytescolab.ms_gateway.exception.domain;

public class AuthenticationException extends GatewayDomainException {

    public AuthenticationException(String message) {
        super("AUTH_FAILED", message);
    }
}
