package es.bytescolab.ms_fixtures.service.support;

import es.bytescolab.ms_fixtures.client.ms_teams.MsTeamsClient;
import es.bytescolab.ms_fixtures.client.ms_teams.dto.TeamDetailsDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamLogoService {

    private final MsTeamsClient msTeamsClient;

    /**
     * Devuelve el logo del equipo obtenido desde ms-teams, o null si no
     * se puede contactar con el servicio.
     */
    @Cacheable(value = "teamLogos", key = "#teamId")
    public String fetchLogo(Integer teamId) {
        try {
            TeamDetailsDto team = msTeamsClient.getTeamById(teamId);
            if (team != null && team.logo() != null && !team.logo().isBlank()) {
                log.debug("Logo obtenido de ms-teams para teamId={}: {}", teamId, team.logo());
                return team.logo();
            }
            log.debug("ms-teams no devolvió logo para teamId={}, se usará fallback", teamId);
            return null;
        } catch (FeignException ex) {
            log.warn("No se pudo contactar ms-teams para teamId={} (status={}). " +
                     "Se usará el logo de api-football como fallback.", teamId, ex.status());
            return null;
        } catch (Exception ex) {
            log.warn("Error inesperado al obtener logo de ms-teams para teamId={}. " +
                     "Se usará el logo de api-football como fallback.", teamId, ex);
            return null;
        }
    }
}
