package es.bytescolab.ms_fixtures.client.apifootball.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureInfo(
        Long id,
        String date,
        Venue venue,
        Status status
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Venue(
            String name,
            String city
    ) {
    }

    /**
     * Estado del partido tal como lo devuelve api-football.
     * <p>
     * La API usa las claves "long" y "short" (palabras reservadas en Java).
     * Se usa @JsonAlias porque es lo que funciona correctamente con la
     * configuración de Jackson/Spring de este proyecto para records.
     * <p>
     * "elapsed" pertenece a este objeto en el JSON real:
     * "status": { "long": "Halftime", "short": "HT", "elapsed": 45 }
     * (no al nivel raíz de fixture como estaba modelado anteriormente).
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Status(
            @JsonAlias("long") String longStatus,
            @JsonAlias("short") String shortStatus,
            Integer elapsed
    ) {
    }
}
