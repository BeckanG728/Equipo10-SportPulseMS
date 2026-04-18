package es.bytescolab.ms_auth.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String tokenType,
        Long expiresIn,
        String userId
) {
}