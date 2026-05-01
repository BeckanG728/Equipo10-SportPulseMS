package es.bytescolab.ms_fixtures.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuración Feign para ms-teams.
 * Propaga el token JWT del request entrante como cabecera Authorization
 * en las llamadas salientes hacia ms-teams, que también requiere autenticación.
 * NOTA: esta clase NO lleva @Configuration a nivel de clase para evitar que
 * Spring la aplique globalmente a todos los FeignClients. Se referencia
 * únicamente desde @FeignClient(configuration = FeignAuthInterceptor.class).
 */
@Slf4j
public class FeignAuthInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";

    @Bean
    public RequestInterceptor jwtPropagationInterceptor() {
        return (RequestTemplate template) -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes == null) {
                log.warn("FeignAuthInterceptor: no hay request activo en el contexto");
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                template.header(HttpHeaders.AUTHORIZATION, authHeader);
            } else {
                log.warn("FeignAuthInterceptor: cabecera Authorization ausente o inválida");
            }
        };
    }
}
