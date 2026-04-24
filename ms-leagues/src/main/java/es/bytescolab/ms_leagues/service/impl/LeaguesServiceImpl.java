package es.bytescolab.ms_leagues.service.impl;

import es.bytescolab.ms_leagues.client.feign.FootballApiClient;
import es.bytescolab.ms_leagues.client.model.ApiResponse;
import es.bytescolab.ms_leagues.dto.response.LeagueDetailsResponse;
import es.bytescolab.ms_leagues.dto.response.LeagueSummaryResponse;
import es.bytescolab.ms_leagues.exception.ExternalApiException;
import es.bytescolab.ms_leagues.exception.LeagueNotFoundException;
import es.bytescolab.ms_leagues.exception.NoResultsFoundException;
import es.bytescolab.ms_leagues.mapper.LeaguesMapper;
import es.bytescolab.ms_leagues.service.LeaguesService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaguesServiceImpl implements LeaguesService {

    private final FootballApiClient footballApiClient;
    private final LeaguesMapper leaguesMapper;

    @Value("${api-football.key}")
    private String apiKey;

    @Override
    @Cacheable(
            value = "leagues",
            key = "(#country != null ? #country : 'ALL') + '_' + (#season != null ? #season : 'ALL')"
    )
    public List<LeagueSummaryResponse> getLeagues(String country, Integer season) {
        log.info("Cache MISS – consultando API-Football: country={}, season={}", country, season);

        ApiResponse apiResponse;

        try {
            apiResponse = footballApiClient.getLeagues(apiKey, country, season);
        } catch (FeignException ex) {
            log.error("Error al conectar con API-Football: {}", ex.getMessage());
            throw new ExternalApiException("No se pudo conectar con la API externa", ex);
        }

        if (apiResponse == null || apiResponse.response() == null || apiResponse.response().isEmpty()) {
            log.warn("API-Football no devolvió resultados para country={}, season={}", country, season);
            throw new NoResultsFoundException(
                    String.format("No se encontraron ligas para los filtros indicados: country=%s, season=%s",
                            country, season)
            );
        }

        List<LeagueSummaryResponse> result = apiResponse.response().stream()
                .map(entry -> leaguesMapper.toLeaguesResponse(entry, season))
                .filter(Objects::nonNull)
                .toList();

        log.info("Se obtuvieron {} ligas de API-Football", result.size());
        return result;
    }

    @Override
    @Cacheable(value = "leagueDetail", key = "#leagueId")
    public LeagueDetailsResponse getLeagueDetail(Integer leagueId) {
        log.info("Cache MISS – consultando detalle de liga id={}", leagueId);

        ApiResponse apiResponse;
        try {
            apiResponse = footballApiClient.getLeagueById(apiKey, leagueId);
        } catch (FeignException ex) {
            log.error("Error al conectar con API-Football: {}", ex.getMessage());
            throw new ExternalApiException("No se pudo conectar con la API externa", ex);
        }

        if (apiResponse == null || apiResponse.response() == null || apiResponse.response().isEmpty()) {
            throw new LeagueNotFoundException("No existe una liga con el ID proporcionado");
        }

        return leaguesMapper.toLeagueDetailResponse(apiResponse.response().get(0));
    }
}
