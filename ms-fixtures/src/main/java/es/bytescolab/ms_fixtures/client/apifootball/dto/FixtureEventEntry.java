package es.bytescolab.ms_fixtures.client.apifootball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Estructura JSON de referencia:
 * {
 * "time":    { "elapsed": 53, "extra": null },
 * "team":    { "id": 210, "name": "Heerenveen", "logo": "https://..." },
 * "player":  { "id": 19313, "name": "Erwin Mulder" },
 * "assist":  { "id": null, "name": null },
 * "type":    "Card",
 * "detail":  "Yellow Card",
 * "comments": null
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureEventEntry(
        EventTime time,
        EventTeam team,
        EventPlayer player,
        EventPlayer assist,
        String type,
        String detail,
        String comments
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EventTime(
            Integer elapsed,
            Integer extra
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EventTeam(
            Integer id,
            String name,
            String logo
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EventPlayer(
            Integer id,
            String name
    ) {
    }
}
