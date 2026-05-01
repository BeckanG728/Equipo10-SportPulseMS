package es.bytescolab.ms_fixtures.client.apifootball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureEntry(
        FixtureInfo fixture,
        LeagueInfo league,
        FixtureTeamInfo teams,
        GoalsInfo goals
) {
}
