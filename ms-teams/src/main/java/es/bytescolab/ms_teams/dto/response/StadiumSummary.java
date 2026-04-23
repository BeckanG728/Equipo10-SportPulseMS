package es.bytescolab.ms_teams.dto.response;

import lombok.Builder;

@Builder
public record StadiumSummary(
        String name,
        String city,
        Integer capacity
) {
}