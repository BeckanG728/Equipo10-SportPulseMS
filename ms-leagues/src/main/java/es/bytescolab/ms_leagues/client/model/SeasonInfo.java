package es.bytescolab.ms_leagues.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonInfo(
        Integer year,
        String start,
        String end,
        Boolean current
) {
}
