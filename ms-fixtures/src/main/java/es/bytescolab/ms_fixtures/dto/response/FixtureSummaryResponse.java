package es.bytescolab.ms_fixtures.dto.response;

public record FixtureSummaryResponse(
        Integer id,
        String date,
        Status status,
        League league,
        TeamFixture homeTeam,
        TeamFixture awayTeam,
        Venue venue
) {
}