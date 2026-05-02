package es.bytescolab.ms_fixtures.dto.common;

public record TeamSummary(
        Integer id,
        String name,
        String logo,
        Integer goals
) {
}