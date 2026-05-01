package es.bytescolab.ms_leagues.controller;

import es.bytescolab.ms_leagues.dto.response.LeagueDetailsResponse;
import es.bytescolab.ms_leagues.dto.response.LeagueSummaryResponse;
import es.bytescolab.ms_leagues.service.LeaguesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leagues")
@RequiredArgsConstructor
public class LeaguesController {

    private final LeaguesService leaguesService;

    @GetMapping
    public ResponseEntity<List<LeagueSummaryResponse>> getLeagues(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer season
    ) {
        List<LeagueSummaryResponse> leagues = leaguesService.getLeagues(country, season);
        return ResponseEntity.ok(leagues);
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueDetailsResponse> getLeagueDetail(
            @PathVariable Integer leagueId
    ) {
        LeagueDetailsResponse league = leaguesService.getLeagueDetail(leagueId);
        return ResponseEntity.ok(league);
    }
}