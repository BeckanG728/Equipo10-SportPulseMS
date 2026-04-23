package es.bytescolab.ms_teams.client.model;

public record TeamInfo(
        Integer id,
        String name,
        String code,
        String country,
        Integer founded,
        Boolean national,
        String logo
) {
}