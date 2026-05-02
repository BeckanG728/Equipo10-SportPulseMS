package es.bytescolab.ms_fixtures.client.apifootball.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeagueInfo(
        Integer id,
        String name,
        String round
) {
}
