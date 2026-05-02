package es.bytescolab.ms_fixtures.dto.response;

import es.bytescolab.ms_fixtures.dto.common.LeagueMatches;
import es.bytescolab.ms_fixtures.dto.common.Status;
import es.bytescolab.ms_fixtures.dto.common.TeamLiveMatches;

public record LiveMatchesResponse(
        Integer id,
        Integer elapsed,
        Status status,
        LeagueMatches league,
        TeamLiveMatches homeTeam,
        TeamLiveMatches awayTeam
) {
}
