package es.bytescolab.ms_gateway.exception.domain;

public class ServiceUnavailableException extends GatewayDomainException {

    public ServiceUnavailableException(String serviceName) {
        super("SERVICE_UNAVAILABLE", "Servicio temporalmente no disponible");
        withMetadata("service", serviceName);
    }
}
