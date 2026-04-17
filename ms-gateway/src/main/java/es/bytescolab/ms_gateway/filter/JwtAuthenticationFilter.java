package es.bytescolab.ms_gateway.filter;

import es.bytescolab.ms_gateway.exception.domain.AuthenticationException;
import es.bytescolab.ms_gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter
        extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            log.debug("Aplicando autenticación JWT en ruta: {}", path);

            String authHeader = request.getHeaders().getFirst("Authorization");

            if (!authHeader.startsWith("Bearer ")) {
                log.warn("Formato de Authorization inválido en ruta: {}", path);
                return Mono.error(new AuthenticationException("Formato de jwt invalido. Bearer <token>"));
            }

            String token = authHeader.substring(7);
            log.debug("Token extraído para validación en ruta: {}", path);

            return Mono.fromCallable(() -> jwtUtil.validateToken(token))
                    .onErrorMap(e -> {
                        log.warn("Token JWT inválido para la ruta {}: {}", path, e.getMessage());
                        return new AuthenticationException("Token JWT inválido o expirado");
                    })
                    .flatMap(claims -> {
                        log.debug("Token validado exitosamente para usuario: {}", claims.getSubject());

                        String userId = claims.getSubject();
                        String userRole = claims.get("role", String.class);

                        ServerHttpRequest mutatedRequest = request.mutate()
                                .header("X-User-Id", userId != null ? userId : "")
                                .header("X-User-Role", userRole != null ? userRole : "")
                                .build();

                        log.debug("Headers de usuario inyectados - UserId: {}, Role: {}", userId, userRole);

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    });
        };
    }

    @Override
    public String name() {
        return "JwtAuthentication";
    }

    public static class Config {
    }
}
