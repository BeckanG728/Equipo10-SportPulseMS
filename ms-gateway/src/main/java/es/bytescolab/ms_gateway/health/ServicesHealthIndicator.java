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
            @Value("${services.auth.url:http://ms-auth:8081/api/actuator/health}") String authUrl,
            @Value("${services.leagues.url:http://ms-leagues:8082/api/actuator/health}") String leaguesUrl,
            @Value("${services.teams.url:http://ms-teams:8083/api/actuator/health}") String teamsUrl,
            @Value("${services.fixtures.url:http://ms-fixtures:8085/api/actuator/health}") String fixturesUrl,
            @Value("${services.standings.url:http://ms-standings:8086/api/actuator/health}") String standingsUrl,
            @Value("${services.notifications.url:http://ms-notifications:8088/api/actuator/health}") String notificationsUrl,
            @Value("${services.dashboard.url:http://ms-dashboard:8089/api/actuator/health}") String dashboardUrl) {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024))
                .build();

        this.services = Map.of(
                "auth", authUrl,
                "leagues", leaguesUrl,
                "teams", teamsUrl,
                "fixtures", fixturesUrl,
                "standings", standingsUrl,
                "notifications", notificationsUrl,
                "dashboard", dashboardUrl);
    }

    @Override
    public Health health() {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("timestamp", java.time.LocalDateTime.now().toString());

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