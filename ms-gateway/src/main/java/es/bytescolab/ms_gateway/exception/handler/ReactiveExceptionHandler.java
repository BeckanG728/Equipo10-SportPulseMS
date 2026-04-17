package es.bytescolab.ms_gateway.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.bytescolab.ms_gateway.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(-1)
public class ReactiveExceptionHandler implements ErrorWebExceptionHandler {

    private final ErrorResponseMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        ErrorContext context = mapper.resolve(ex);
        logError(context, ex, exchange.getRequest().getPath().value());

        ErrorResponseDto response = new ErrorResponseDto(
            context.getErrorCode(),
            context.getMessage(),
            context.getMetadata(),
            Instant.now()
        );

        return writeResponse(exchange, context.getStatus(), response);
    }

    private void logError(ErrorContext ctx, Throwable ex, String path) {
        if (ctx.getStatus().is5xxServerError()) {
            log.error("[{}] {} en {}", ctx.getStatus().value(), ctx.getErrorCode(), path, ex);
        } else {
            log.warn("[{}] {} en {}: {}", ctx.getStatus().value(), ctx.getErrorCode(), path, ex.getMessage());
        }
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, HttpStatus status, ErrorResponseDto response) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(response))
            .flatMap(bytes -> {
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                return exchange.getResponse().writeWith(Mono.just(buffer));
            })
            .onErrorResume(e -> {
                log.error("Error serializando respuesta de error", e);
                return exchange.getResponse().setComplete();
            });
    }
}
