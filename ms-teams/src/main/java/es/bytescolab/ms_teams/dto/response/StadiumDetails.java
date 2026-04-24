package es.bytescolab.ms_teams.dto.response;

import lombok.Builder;

@Builder
public record StadiumDetails(
        String name,
        String address,
        String city,
        Integer capacity,
        String surface
) {
}
