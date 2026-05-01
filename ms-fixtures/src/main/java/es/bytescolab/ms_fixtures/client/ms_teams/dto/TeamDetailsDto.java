package es.bytescolab.ms_fixtures.client.ms_teams.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Respuesta mínima de ms-teams — solo usamos id y logo para el enriquecimiento.
 * El resto de campos se ignoran deliberadamente
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamDetailsDto(
        Integer id,
        String name,
        String logo
) {
}
