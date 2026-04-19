package es.bytescolab.ms_gateway.filter;

import es.bytescolab.ms_gateway.exception.domain.AuthenticationException;
import es.bytescolab.ms_gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
public class JwtGatewayFilter
        extends AbstractGatewayFilterFactory<JwtGatewayFilter.Config> {

    private final JwtUtil jwtUtil;

    public JwtGatewayFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // Validar rutas publicas
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

            if (route == null) return chain.filter(exchange);

            Boolean isPublic = (Boolean) route.getMetadata().getOrDefault("public", false);

            if (Boolean.TRUE.equals(isPublic)) return chain.filter(exchange);

            // Rutas protegidas
            String auth = request.getHeaders().getFirst("Authorization");

            // Validacion de presencia y formato del token
            if (auth == null) {
                log.warn("Formato de Authorization inválido en ruta: {}", path);
                return Mono.error(new AuthenticationException("Token no encontrado."));
            }

            if (!auth.startsWith("Bearer ")) {
                log.warn("Formato de Authorization inválido en ruta: {}", path);
                return Mono.error(new AuthenticationException("Formato de jwt invalido. Bearer <token>"));
            }

            // Se recupera el token
            String token = auth.substring(7);

            return Mono.fromCallable(() -> jwtUtil.validateToken(token))
                    .subscribeOn(Schedulers.boundedElastic())
                    .map(claims -> {

                        String userId = claims.getSubject();
                        String role = claims.get("role", String.class);

                        ServerHttpRequest mutated = request.mutate()
                                .header("X-User-Id", userId)
                                .header("X-User-Role", role)
                                .build();

                        return exchange.mutate().request(mutated).build();
                    })
                    .onErrorMap(e ->
                            new AuthenticationException("Token invalido o expirado")
                    )
                    .flatMap(chain::filter);
        };
    }

    public static class Config {
    }
}
