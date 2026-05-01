package es.bytescolab.ms_fixtures.client.apifootball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiResponse(
        List<FixtureEntry> response
) {
}
