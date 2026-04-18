package es.bytescolab.ms_gateway.exception.handler;

@FunctionalInterface
public interface ErrorResponseStrategy {
    ErrorContext map(Throwable ex);
}
