package es.bytescolab.ms_fixtures.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache de fixtures: TTL corto (30 min) porque los datos del partido
     * cambian durante el día (goles, estado).
     *
     * Cache de logos: TTL largo (24 h) porque los logos de equipos son
     * estables y queremos minimizar las llamadas a ms-teams, que a su vez
     * consume el cupo de 100 requests/día de la API externa.
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();

        manager.registerCustomCache("fixtures",
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .maximumSize(100)
                        .build());

        manager.registerCustomCache("teamLogos",
                Caffeine.newBuilder()
                        .expireAfterWrite(24, TimeUnit.HOURS)
                        .maximumSize(500)
                        .build());

        return manager;
    }
}
