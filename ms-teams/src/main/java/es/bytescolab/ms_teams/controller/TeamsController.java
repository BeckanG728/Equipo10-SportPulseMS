package es.bytescolab.ms_teams.controller;

import es.bytescolab.ms_teams.dto.response.TeamDetailsResponse;
import es.bytescolab.ms_teams.dto.response.TeamSummaryResponse;
import es.bytescolab.ms_teams.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
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

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDetailsResponse> getTeamDetails(
            @PathVariable Integer teamId
    ) {
        TeamDetailsResponse response = teamService.getTeamDetails(teamId);
        return ResponseEntity.ok(response);
    }
}
