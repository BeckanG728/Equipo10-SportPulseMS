package es.bytescolab.ms_fixtures.exception;

import es.bytescolab.ms_fixtures.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";
        String message = "Valor inválido para el parámetro '" + paramName + "': " + value;
        log.warn("Error de conversión de parámetro: {}", message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError("INVALID_PARAMETER", message));
    }

    @ExceptionHandler(FixturesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(FixturesNotFoundException ex) {
        log.info("Sin resultados: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError("FIXTURE_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponse> handleExternalApi(ExternalApiException ex) {
        log.error("Error al llamar a la API externa: {}", ex.getMessage(), ex.getCause());
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(buildError("EXTERNAL_API_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError("INTERNAL_ERROR", "Ocurrió un error inesperado"));
    }

    private ErrorResponse buildError(String error, String message) {
        return ErrorResponse.builder()
                .error(error)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }
}
