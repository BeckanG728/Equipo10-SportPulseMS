package es.bytescolab.ms_auth.dto.response;

import lombok.Builder;

@Builder
public record ValidateResponse(
        boolean valid,
        String userId,
        String username,
        String role,
        String error,
        String message
) {
}
