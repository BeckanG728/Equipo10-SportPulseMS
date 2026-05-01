package es.bytescolab.ms_fixtures.client.apifootball.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureTeamInfo(
        Team home,
        Team away
) {

    /**
     * Datos del equipo dentro de un fixture tal como los devuelve api-football.
     * <p>
     * IMPORTANTE: el campo "goals" NO existe aquí en el JSON real.
     * Los goles vienen en el objeto "goals" al nivel raíz de cada entrada
     * (campo GoalsInfo en FixtureEntry), por lo que se leen desde ahí en el mapper.
     * Aquí solo se modela lo que la API realmente incluye en teams.home / teams.away:
     * id, name, logo y winner.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Team(
            Integer id,
            String name,
            String logo,
            Boolean winner
    ) {
    }
}
