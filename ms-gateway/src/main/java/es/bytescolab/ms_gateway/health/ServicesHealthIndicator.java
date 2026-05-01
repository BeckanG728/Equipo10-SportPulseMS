package es.bytescolab.ms_gateway.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ServicesHealthIndicator implements HealthIndicator {

    private final WebClient webClient;
    private final Map<String, String> services;

    public ServicesHealthIndicator(
            @Value("${services.auth.url}") String authUrl,
            @Value("${services.leagues.url}") String leaguesUrl,
            @Value("${services.teams.url}") String teamsUrl,
            @Value("${services.fixtures.url}") String fixturesUrl,
            @Value("${services.standings.url}") String standingsUrl,
            @Value("${services.notifications.url}") String notificationsUrl,
            @Value("${services.dashboard.url}") String dashboardUrl) {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024))
                .build();

        this.services = Map.of(
                "ms-auth", authUrl,
                "ms-leagues", leaguesUrl,
                "ms-teams", teamsUrl,
                "ms-fixtures", fixturesUrl,
                "ms-standings", standingsUrl,
                "ms-notifications", notificationsUrl,
                "ms-dashboard", dashboardUrl);
    }

    @Override
    public Health health() {
        Map<String, Object> details = new LinkedHashMap<>();

        boolean allUp = true;

        for (Map.Entry<String, String> entry : services.entrySet()) {
            boolean isUp = checkService(entry.getValue());
            details.put(entry.getKey(), isUp ? "UP" : "DOWN");
            if (!isUp)
                allUp = false;
        }

        if (allUp) {
            return Health.up().withDetails(details).build();
        }
        return Health.down().withDetails(details).build();
    }

    private boolean checkService(String url) {
        try {
            webClient.get().uri(url)
                    .retrieve()
                    .toBodilessEntity()
                    .block(Duration.ofSeconds(3));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}