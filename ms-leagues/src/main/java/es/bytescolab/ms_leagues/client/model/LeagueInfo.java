package es.bytescolab.ms_leagues.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeagueInfo(
        Integer id,
        String name,
        String type,
        String logo
) {
}
