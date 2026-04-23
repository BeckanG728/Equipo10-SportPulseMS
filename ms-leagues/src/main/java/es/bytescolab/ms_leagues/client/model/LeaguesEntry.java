package es.bytescolab.ms_leagues.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeaguesEntry(
        LeagueInfo league,
        CountryInfo country,
        List<SeasonInfo> seasons
) {
}
