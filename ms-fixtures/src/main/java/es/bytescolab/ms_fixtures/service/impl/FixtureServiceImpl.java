package es.bytescolab.ms_fixtures.service.impl;

import es.bytescolab.ms_fixtures.client.apifootball.ApiFootballClient;
import es.bytescolab.ms_fixtures.client.apifootball.dto.ApiResponse;
import es.bytescolab.ms_fixtures.client.apifootball.dto.FixtureEntry;
import es.bytescolab.ms_fixtures.dto.request.FixtureFilterRequest;
import es.bytescolab.ms_fixtures.dto.response.FixtureSummaryResponse;
import es.bytescolab.ms_fixtures.exception.ExternalApiException;
import es.bytescolab.ms_fixtures.exception.FixturesNotFoundException;
import es.bytescolab.ms_fixtures.mapper.FixtureMapper;
import es.bytescolab.ms_fixtures.service.FixtureService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FixtureServiceImpl implements FixtureService {

    private final ApiFootballClient apiFootballClient;
    private final FixtureMapper fixtureMapper;
    private final TeamLogoService teamLogoService;

    @Value("${api-football.key}")
    private String apiKey;

    @Override
    @Cacheable(
            value = "fixtures",
            key = "(#filter.league() ?: 'null') + ':' + (#filter.team() ?: 'null') + ':' + (#filter.date() ?: 'null') + ':' + (#filter.status() ?: 'null')"
    )
    public List<FixtureSummaryResponse> getFixtures(FixtureFilterRequest filter) {
        FixtureFilterRequest resolved = resolveFilter(filter);

        log.info("[CACHE MISS] Consultando fixtures — league={}, team={}, date={}, status={}",
                resolved.league(), resolved.team(), resolved.date(), resolved.status());

        ApiResponse apiResponse = fetchFromExternalApi(resolved);

        log.info("Respuesta de API externa — total fixtures: {}",
                apiResponse.response() == null ? "null" : apiResponse.response().size());

        if (apiResponse.response() == null || apiResponse.response().isEmpty()) {
            throw new FixturesNotFoundException();
        }

        return apiResponse.response().stream()
                .map(this::enrichAndMap)
                .collect(Collectors.toList());
    }


    // Resolucion del filtro con los parametros recibidos del cliente

    /**
     * Determina la fecha efectiva a enviar a la API según los filtros con los que se cuenta
     */
    private FixtureFilterRequest resolveFilter(FixtureFilterRequest filter) {
        boolean hasLeagueOrTeam = filter.league() != null || filter.team() != null;

        LocalDate date;
        if (hasLeagueOrTeam) {
            // Con league/team: asigna fecha especificada o null
            date = filter.date();
        } else {
            // Sin league/team, con o sin status: normalizar a hoy o fecha especificada
            date = filter.date() != null ? filter.date() : LocalDate.now();
        }

        return new FixtureFilterRequest(
                filter.league(),
                filter.team(),
                date,
                filter.status()
        );
    }


    // Llamada a API externa
    private ApiResponse fetchFromExternalApi(FixtureFilterRequest filter) {
        boolean hasLeagueOrTeam = filter.league() != null || filter.team() != null;

        Integer season = null;
        if (hasLeagueOrTeam) {
            // Si el usuario proporcionó una fecha, el season se deriva de ella.
            // Si no, se usa 2024 como temporada más reciente disponible
            // en el plan gratuito de api-football (rango disponible: 2022-2024).
            season = filter.date() != null
                    ? filter.date().getYear()
                    : 2024;
        }

        String statusCode = filter.status() != null
                ? filter.status().toString()
                : null;

        log.debug("Llamando a api-football — league={}, team={}, season={}, date={}, status={}",
                filter.league(), filter.team(), season, filter.date(), statusCode);

        try {
            return apiFootballClient.getFixtures(
                    apiKey,
                    filter.league(),
                    filter.team(),
                    season,
                    filter.date(),
                    statusCode
            );
        } catch (FeignException ex) {
            log.error("FeignException llamando a api-football — status={}, body={}",
                    ex.status(), ex.contentUTF8());
            throw new ExternalApiException(
                    "Error al obtener partidos de la API externa: " + ex.status(), ex
            );
        }
    }


    // Enriquecimiento con logo de ms-teams (fallback)

    /**
     * Estrategia de logo:
     * 1. Se usa el logo que viene directamente en la respuesta del endpoint /fixtures de api-football
     * 2. Solo si ese logo es null o vacío, se consulta ms-teams como fallback
     * <p>
     * Esto preserva el cupo de 100 requests/día: ms-teams solo se llama
     * cuando el logo no esta disponible desde el endpoint /fixtures
     */
    private FixtureSummaryResponse enrichAndMap(FixtureEntry entry) {
        String homeLogoFromFixture = entry.teams().home().logo();
        String awayLogoFromFixture = entry.teams().away().logo();

        String homeLogo = resolveLogoWithFallback(entry.teams().home().id(), homeLogoFromFixture);
        String awayLogo = resolveLogoWithFallback(entry.teams().away().id(), awayLogoFromFixture);

        return fixtureMapper.toResponse(entry, homeLogo, awayLogo);
    }

    /**
     * Devuelve el logo de api-football si está disponible.
     * Si es null o vacío, consulta ms-teams como fallback.
     */
    private String resolveLogoWithFallback(Integer teamId, String logoFromFixture) {
        if (logoFromFixture != null && !logoFromFixture.isBlank()) {
            return logoFromFixture;
        }
        log.debug("Logo ausente en fixture para teamId={}, consultando ms-teams", teamId);
        return teamLogoService.fetchLogo(teamId);
    }
}
