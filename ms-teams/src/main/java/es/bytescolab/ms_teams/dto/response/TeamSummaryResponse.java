package es.bytescolab.ms_teams.dto.response;

import lombok.Builder;

@Builder
public record TeamSummaryResponse(
        Integer id,
        String name,
        String country,
        String logo,
        Integer founded,
        StadiumSummary stadium
) {
}