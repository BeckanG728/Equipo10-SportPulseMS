package es.bytescolab.ms_auth.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,      // "Not Found", "Bad Request", etc.
        String message,    // mensaje legible
        String path        // URI que lo originó
) {
}
