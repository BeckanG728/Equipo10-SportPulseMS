package es.bytescolab.ms_fixtures.client.ms_teams;

import es.bytescolab.ms_fixtures.client.ms_teams.dto.TeamDetailsDto;
import es.bytescolab.ms_fixtures.config.FeignAuthInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ms-teams",
        url = "${ms-teams.host-url}",
        configuration = FeignAuthInterceptor.class
)
public interface MsTeamsClient {

    @GetMapping("/api/teams/{teamId}")
    TeamDetailsDto getTeamById(@PathVariable("teamId") Integer teamId);
}
