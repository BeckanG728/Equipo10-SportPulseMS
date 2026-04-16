package es.bytescolab.ms_auth.dto.request;

public record LoginRequest(

        String username,
        String password
) {
}
