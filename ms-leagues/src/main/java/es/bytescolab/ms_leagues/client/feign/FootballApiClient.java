package es.bytescolab.ms_leagues.client.feign;

import es.bytescolab.ms_leagues.client.model.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "football-api", url = "${api-football.host-url}")
public interface FootballApiClient {

    @GetMapping("/leagues")
    ApiResponse getLeagues(
            @RequestHeader("x-apisports-key") String apiKey,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "season", required = false) Integer season
    );
}