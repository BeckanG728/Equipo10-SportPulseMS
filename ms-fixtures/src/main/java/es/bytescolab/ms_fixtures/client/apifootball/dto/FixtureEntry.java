package es.bytescolab.ms_fixtures.client.apifootball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.bytescolab.ms_fixtures.client.apifootball.dto.common.FixtureInfo;
import es.bytescolab.ms_fixtures.client.apifootball.dto.common.FixtureTeamInfo;
import es.bytescolab.ms_fixtures.client.apifootball.dto.common.GoalsInfo;
import es.bytescolab.ms_fixtures.client.apifootball.dto.common.LeagueInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureEntry(
        FixtureInfo fixture,
        LeagueInfo league,
        FixtureTeamInfo teams,
        GoalsInfo goals
) {
}
