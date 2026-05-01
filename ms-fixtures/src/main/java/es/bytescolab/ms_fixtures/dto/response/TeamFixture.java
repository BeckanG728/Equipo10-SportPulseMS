package es.bytescolab.ms_fixtures.dto.response;

public record TeamFixture(
        Integer id,
        String name,
        String logo,
        Integer goals
) {
}