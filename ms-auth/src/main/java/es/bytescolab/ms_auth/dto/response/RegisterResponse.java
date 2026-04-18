package es.bytescolab.ms_auth.dto.response;

import es.bytescolab.ms_auth.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String username,
        String email,
        UserRole role,
        Instant createdAt
) {
}
