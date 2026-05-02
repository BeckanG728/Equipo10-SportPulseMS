package es.bytescolab.ms_fixtures.service;

import es.bytescolab.ms_fixtures.dto.request.FixtureFilterRequest;
import es.bytescolab.ms_fixtures.dto.response.FixtureEventResponse;
import es.bytescolab.ms_fixtures.dto.response.FixtureSummaryResponse;
import es.bytescolab.ms_fixtures.dto.response.LiveMatchesResponse;

import java.util.List;

public interface FixtureService {
    List<FixtureSummaryResponse> getFixtures(FixtureFilterRequest filter);

    List<LiveMatchesResponse> getLiveMatches();

    List<FixtureEventResponse> getEvents(Integer fixtureId);
}
