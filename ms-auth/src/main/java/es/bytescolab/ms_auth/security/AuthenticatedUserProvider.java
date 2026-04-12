package es.bytescolab.ms_auth.security;

import es.bsager.ms_auth.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserProvider {

    private final JwtUtil jwtUtil;

    private String extractToken() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) throw new UserNotFoundException("No hay request en curso");

        String header = attrs.getRequest().getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer "))
            throw new UserNotFoundException("No hay token en el request");

        return header.substring(7);
    }

    public UUID getCurrentUserId() {
        return UUID.fromString(jwtUtil.extractUserId(extractToken()));
    }

    public String getCurrentUsername() {
        return jwtUtil.extractUsername(extractToken());
    }

    public String getCurrentRole() {
        return jwtUtil.extractRole(extractToken());
    }
}