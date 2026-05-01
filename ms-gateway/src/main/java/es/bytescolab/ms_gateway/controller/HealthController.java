package es.bytescolab.ms_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("status", "UP");
        response.put("services", Map.of(
                "auth", "UP",
                "leagues", "UP",
                "teams", "UP",
                "fixtures", "UP",
                "standings", "UP",
                "notifications", "UP",
                "dashboard", "UP"));
        return response;
    }
}