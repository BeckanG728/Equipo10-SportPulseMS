package es.bytescolab.ms_teams.service.impl;

import es.bytescolab.ms_teams.client.feign.FootballApiClient;
import es.bytescolab.ms_teams.client.model.ApiResponse;
import es.bytescolab.ms_teams.dto.response.TeamDetailsResponse;
import es.bytescolab.ms_teams.dto.response.TeamSummaryResponse;
import es.bytescolab.ms_teams.exception.ExternalApiException;
import es.bytescolab.ms_teams.exception.NoResultsFoundException;
import es.bytescolab.ms_teams.exception.TeamNotFoundException;
import es.bytescolab.ms_teams.mapper.TeamsMapper;
import es.bytescolab.ms_teams.service.TeamService;
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
public class TeamServiceImpl implements TeamService {

    private final FootballApiClient footballApiClient;
    private final TeamsMapper teamsMapper;

    @Value("${api-football.key}")
    private String apiKey;

    @Override
    @Cacheable(
            value = "teams",
            key = "(#leagueId + '_' + #season)"
    )
    public List<TeamSummaryResponse> getTeamSummary(Integer leagueId, Integer season) {
        log.info("Cache MISS – consultando API-Football: league={}, season={}", leagueId, season);
        ApiResponse apiResponse;

        try {
            apiResponse = footballApiClient.getTeams(apiKey, leagueId, season);
        } catch (FeignException ex) {
            log.error("Error al conectar con API-Football: {}", ex.getMessage());
            throw new ExternalApiException("No se pudo conectar con la API externa", ex);
        }

        if (apiResponse == null || apiResponse.response() == null || apiResponse.response().isEmpty()) {
            log.warn("API-Football no devolvió resultados para league={}, season={}", leagueId, season);
            throw new NoResultsFoundException(
                    String.format("No se encontraron ligas para los filtros indicados: league=%s, season=%s",
                            leagueId, season)
            );
        }

        List<TeamSummaryResponse> result = apiResponse.response().stream()
                .map(teamsMapper::toSummaryResponse)
                .filter(Objects::nonNull)
                .toList();

        log.info("Se obtuvieron {} equipos de API-Football", result.size());
        return result;
    }

    @Override
    @Cacheable(value = "teamDetail", key = "(#teamId)")
    public TeamDetailsResponse getTeamDetails(Integer teamId) {
        log.info("Cache MISS – consultando API-Football: team={}", teamId);
        ApiResponse apiResponse;

        try {
            apiResponse = footballApiClient.getTeamsById(apiKey, teamId);
        } catch (FeignException ex) {
            log.error("Error al conectar con API-Football: {}", ex.getMessage());
            throw new ExternalApiException("No se pudo conectar con la API externa", ex);
        }

        if (apiResponse == null || apiResponse.response() == null || apiResponse.response().isEmpty()) {
            log.warn("API-Football no devolvió resultados para team={}", teamId);
            throw new TeamNotFoundException("No existe un equipo con el ID proporcionado");
        }
        return teamsMapper.toDetailsResponse(apiResponse.response().get(0));
    }
}
