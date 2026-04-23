package es.bytescolab.ms_teams.client.feign;

import es.bytescolab.ms_teams.client.model.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "football-api", url = "${api-football.host-url}")
public interface FootballApiClient {

    @GetMapping("/teams")
    ApiResponse getTeams(
            @RequestHeader("x-apisports-key") String apiKey,
            @RequestParam(value = "league") Integer league,
            @RequestParam(value = "season") Integer season
    );

    @GetMapping("/teams")
    ApiResponse getTeamsById(
            @RequestHeader("x-apisports-key") String apiKey,
            @RequestParam(value = "id") Integer id
    );
}
