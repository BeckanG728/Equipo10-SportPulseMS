package es.bytescolab.ms_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    /**
     * replenishRate = 1  → se añade 1 token por segundo al bucket <br>
     * burstCapacity  = 60 → máximo de tokens acumulables (ráfaga de hasta 60 req) <br>
     * requestedTokens = 1 → cada request consume 1 token
     */
    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 60, 1);
    }
}
