package es.bytescolab.ms_fixtures.service;

import es.bytescolab.ms_fixtures.dto.request.FixtureFilterRequest;
import es.bytescolab.ms_fixtures.dto.response.FixtureSummaryResponse;

import java.util.List;

public interface FixtureService {
    List<FixtureSummaryResponse> getFixtures(FixtureFilterRequest filter);
}
