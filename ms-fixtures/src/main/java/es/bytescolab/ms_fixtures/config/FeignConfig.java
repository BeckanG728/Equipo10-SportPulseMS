package es.bytescolab.ms_fixtures.config;

import feign.Logger;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import java.time.format.DateTimeFormatter;

@Configuration
public class FeignConfig {

    @Bean
    public FeignFormatterRegistrar feignDateFormatter() {
        return registry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setDateFormatter(DateTimeFormatter.ISO_LOCAL_DATE);
            registrar.registerFormatters(registry);
        };
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
