package es.bytescolab.ms_leagues.mapper;

import es.bytescolab.ms_leagues.client.model.LeaguesEntry;
import es.bytescolab.ms_leagues.client.model.SeasonInfo;
import es.bytescolab.ms_leagues.dto.response.LeagueDetailResponse;
import es.bytescolab.ms_leagues.dto.response.LeaguesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class LeaguesMapper {

    public LeaguesResponse toLeaguesResponse(LeaguesEntry entry, Integer seasonFilter) {
        if (entry.league() == null) return null;

        SeasonInfo season = pickCurrentSeason(entry.seasons(), seasonFilter);

        return LeaguesResponse.builder()
                .id(entry.league().id())
                .name(entry.league().name())
                .type(entry.league().type())
                .logo(entry.league().logo())
                .country(entry.country() != null ? entry.country().name() : null)
                .currentSeason(season != null ? season.year() : null)
                .startDate(season != null ? parseDate(season.start()) : null)
                .endDate(season != null ? parseDate(season.end()) : null)
                .build();
    }

    public LeagueDetailResponse toLeagueDetailResponse(LeaguesEntry entry) {
        if (entry.league() == null) return null;

        SeasonInfo season = pickCurrentSeason(entry.seasons(), null);

        List<Integer> seasonYears = entry.seasons() == null ? List.of() :
                entry.seasons().stream()
                        .map(SeasonInfo::year)
                        .filter(Objects::nonNull)
                        .toList();

        return LeagueDetailResponse.builder()
                .id(entry.league().id())
                .name(entry.league().name())
                .type(entry.league().type())
                .logo(entry.league().logo())
                .country(entry.country() != null ? entry.country().name() : null)
                .seasons(seasonYears)
                .currentSeason(currentSeason(season))
                .build();
    }

    // Helpers
    private LeagueDetailResponse.CurrentSeason currentSeason(SeasonInfo current) {
        return current == null ? null :
                LeagueDetailResponse.CurrentSeason.builder()
                        .year(current.year())
                        .startDate(parseDate(current.start()))
                        .endDate(parseDate(current.end()))
                        .current(current.current())
                        .build();
    }

    private SeasonInfo pickCurrentSeason(List<SeasonInfo> seasons, Integer seasonFilter) {
        if (seasons == null || seasons.isEmpty()) return null;

        // 1. Si se solicitó una temporada concreta, buscarla por año
        if (seasonFilter != null) {
            return seasons.stream()
                    .filter(s -> seasonFilter.equals(s.year()))
                    .findFirst()
                    .orElseGet(() -> {
                        log.warn("No se encontró la temporada {} en la lista; se usa la más reciente", seasonFilter);
                        return seasons.get(seasons.size() - 1);
                    });
        }

        // 2. Sin filtro: preferir la marcada como actual, si no la más reciente
        return seasons.stream()
                .filter(s -> Boolean.TRUE.equals(s.current()))
                .findFirst()
                .orElse(seasons.get(seasons.size() - 1));
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            log.warn("No se pudo parsear la fecha '{}', se retorna null", dateStr);
            return null;
        }
    }
}
