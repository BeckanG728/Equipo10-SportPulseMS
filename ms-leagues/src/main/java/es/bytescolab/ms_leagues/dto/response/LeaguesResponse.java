package es.bytescolab.ms_leagues.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LeaguesResponse(
        Integer id,
        String name,
        String type,
        String country,
        String logo,
        Integer currentSeason,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate
) {
}