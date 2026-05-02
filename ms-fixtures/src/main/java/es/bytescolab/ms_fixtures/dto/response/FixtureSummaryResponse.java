package es.bytescolab.ms_fixtures.dto.response;

import es.bytescolab.ms_fixtures.dto.common.LeagueSummary;
import es.bytescolab.ms_fixtures.dto.common.Status;
import es.bytescolab.ms_fixtures.dto.common.TeamSummary;
import es.bytescolab.ms_fixtures.dto.common.Venue;

public record FixtureSummaryResponse(
        Integer id,
        String date,
        Status status,
        LeagueSummary league,
        TeamSummary homeTeam,
        TeamSummary awayTeam,
        Venue venue
) {
}