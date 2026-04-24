package es.bytescolab.ms_leagues.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record LeagueDetailsResponse(
        Integer id,
        String name,
        String type,
        String country,
        String logo,
        List<Integer> seasons,
        CurrentSeason currentSeason
) {

    @Builder
    public record CurrentSeason(
            Integer year,
            @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate startDate,
            @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate endDate,
            Boolean current
    ) {
    }
}
