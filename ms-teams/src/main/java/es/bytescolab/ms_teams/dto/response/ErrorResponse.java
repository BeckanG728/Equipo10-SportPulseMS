package es.bytescolab.ms_teams.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(
        String error,
        String message,
        Instant timestamp
) {
}

