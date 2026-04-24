package es.bytescolab.ms_teams.dto.response;

import lombok.Builder;

@Builder
public record TeamDetailsResponse(
        Integer id,
        String name,
        String country,
        String logo,
        Integer founded,
        Boolean national,
        StadiumDetails stadium
) {
}