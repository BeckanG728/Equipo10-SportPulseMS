package es.bytescolab.ms_leagues.controller;

import es.bytescolab.ms_leagues.dto.response.LeagueDetailResponse;
import es.bytescolab.ms_leagues.dto.response.LeaguesResponse;
import es.bytescolab.ms_leagues.service.LeaguesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class LeaguesController {

    private final LeaguesService leaguesService;

    @GetMapping
    public ResponseEntity<List<LeaguesResponse>> getLeagues(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer season) {

        List<LeaguesResponse> leagues = leaguesService.getLeagues(country, season);
        return ResponseEntity.ok(leagues);
    }


    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueDetailResponse> getLeagueDetail(
            @PathVariable Integer leagueId
    ) {
        LeagueDetailResponse league = leaguesService.getLeagueDetail(leagueId);
        return ResponseEntity.ok(league);
    }
}