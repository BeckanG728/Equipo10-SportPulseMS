package es.bytescolab.ms_fixtures.client.apifootball.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoalsInfo(
        Integer home,
        Integer away
) {
}
