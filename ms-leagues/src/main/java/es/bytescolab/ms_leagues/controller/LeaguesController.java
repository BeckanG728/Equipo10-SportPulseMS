package es.bytescolab.ms_leagues.controller;

import es.bytescolab.ms_leagues.dto.response.LeaguesResponse;
import es.bytescolab.ms_leagues.service.impl.LeaguesServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class LeaguesController {

    private final LeaguesServiceImpl leaguesServiceImpl;

    @GetMapping
    public ResponseEntity<List<LeaguesResponse>> getLeagues(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer season) {

        List<LeaguesResponse> leagues = leaguesServiceImpl.getLeagues(country, season);
        return ResponseEntity.ok(leagues);
    }
}