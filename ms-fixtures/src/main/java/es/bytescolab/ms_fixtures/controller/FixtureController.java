package es.bytescolab.ms_fixtures.controller;

import es.bytescolab.ms_fixtures.dto.request.FixtureFilterRequest;
import es.bytescolab.ms_fixtures.dto.response.FixtureSummaryResponse;
import es.bytescolab.ms_fixtures.dto.response.LiveMatchesResponse;
import es.bytescolab.ms_fixtures.enums.FixtureStatus;
import es.bytescolab.ms_fixtures.service.FixtureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fixtures")
@RequiredArgsConstructor
public class FixtureController {

    private final FixtureService fixtureService;

    @GetMapping
    public ResponseEntity<List<FixtureSummaryResponse>> getFixtures(
            @RequestParam(required = false) Integer league,
            @RequestParam(required = false) Integer team,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) FixtureStatus status
    ) {
        FixtureFilterRequest filter = new FixtureFilterRequest(league, team, date, status);
        return ResponseEntity.ok(fixtureService.getFixtures(filter));
    }

    @GetMapping("/live")
    public ResponseEntity<List<LiveMatchesResponse>> getLiveMatches() {
        List<LiveMatchesResponse> response = fixtureService.getLiveMatches();
        return ResponseEntity.ok(response);
    }

}
