package es.bytescolab.ms_teams.controller;

import es.bytescolab.ms_teams.dto.response.TeamSummaryResponse;
import es.bytescolab.ms_teams.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/teams")
@RequiredArgsConstructor
public class TeamsController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamSummaryResponse>> listTeams(
            @RequestParam("league") Integer leagueId,
            @RequestParam Integer season
    ) {
        List<TeamSummaryResponse> response = teamService.getTeamSummary(leagueId, season);
        return ResponseEntity.ok(response);
    }
}
