package es.bytescolab.ms_fixtures.dto.request;

import es.bytescolab.ms_fixtures.enums.FixtureStatus;

import java.time.LocalDate;

public record FixtureFilterRequest(
        Integer league,
        Integer team,
        LocalDate date,
        FixtureStatus status
) {
}