package es.bytescolab.ms_fixtures.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.bytescolab.ms_fixtures.dto.response.ErrorResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        Optional<Claims> claims = jwtUtil.validate(token);

        if (claims.isEmpty()) {
            log.warn("Token inválido o expirado — URI: {}", request.getRequestURI());
            rejectUnauthorized(response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext()
                    .setAuthentication(buildAuthentication(claims.get()));
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken buildAuthentication(Claims claims) {
        String subject = claims.getSubject();
        log.debug("Request autenticada — subject='{}'", subject);
        return new UsernamePasswordAuthenticationToken(subject, null, List.of());
    }

    private void rejectUnauthorized(HttpServletResponse response) throws IOException {
        ErrorResponse body = ErrorResponse.builder()
                .error("TOKEN_INVALID")
                .message("El token JWT es inválido o ha expirado")
                .timestamp(Instant.now())
                .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
