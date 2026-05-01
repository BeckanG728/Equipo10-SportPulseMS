package es.bytescolab.ms_fixtures.config;

import es.bytescolab.ms_fixtures.enums.FixtureStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToFixtureStatusConverter());
    }

    private static class StringToFixtureStatusConverter implements Converter<String, FixtureStatus> {
        @Override
        public FixtureStatus convert(@NonNull String source) {
            return FixtureStatus.from(source);
        }
    }
}
