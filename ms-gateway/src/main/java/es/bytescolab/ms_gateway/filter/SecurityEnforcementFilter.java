package es.bytescolab.ms_gateway.filter;

import es.bytescolab.ms_gateway.exception.domain.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-1)
public class SecurityEnforcementFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        
        if (route == null) {
            return chain.filter(exchange);
        }

        Boolean isPublic = (Boolean) route.getMetadata().getOrDefault("public", false);
        String path = exchange.getRequest().getURI().getPath();

        log.debug("Enforcement - Ruta: {}, Pública: {}", path, isPublic);

        if (!isPublic && !exchange.getRequest().getHeaders().containsKey("Authorization")) {
            log.warn("Acceso denegado a ruta protegida sin Authorization: {}", path);
            return Mono.error(new AuthenticationException("Acceso denegado"));
        }

        return chain.filter(exchange);
    }
}
