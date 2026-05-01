package es.bytescolab.ms_fixtures.mapper;

import es.bytescolab.ms_fixtures.client.apifootball.dto.FixtureEntry;
import es.bytescolab.ms_fixtures.dto.response.*;
import org.springframework.stereotype.Component;

@Component
public class FixtureMapper {

    /**
     * Mapea un FixtureEntry al DTO de respuesta.
     *
     * @param entry    entrada cruda de api-football
     * @param homeLogo logo del equipo local ya resuelto
     * @param awayLogo logo del equipo visitante ya resuelto
     */
    public FixtureSummaryResponse toResponse(FixtureEntry entry,
                                             String homeLogo,
                                             String awayLogo) {
        Integer homeGoals = entry.goals() != null ? entry.goals().home() : null;
        Integer awayGoals = entry.goals() != null ? entry.goals().away() : null;

        return new FixtureSummaryResponse(
                entry.fixture().id().intValue(),
                entry.fixture().date(),
                toStatus(entry),
                toLeague(entry),
                toTeam(entry.teams().home().id(), entry.teams().home().name(), homeLogo, homeGoals),
                toTeam(entry.teams().away().id(), entry.teams().away().name(), awayLogo, awayGoals),
                toVenue(entry)
        );
    }

    private Status toStatus(FixtureEntry entry) {
        return new Status(
                entry.fixture().status().shortStatus(),
                entry.fixture().status().longStatus()
        );
    }

    private League toLeague(FixtureEntry entry) {
        return new League(
                entry.league().id(),
                entry.league().name(),
                entry.league().round()
        );
    }

    private TeamFixture toTeam(Integer id, String name, String logo, Integer goals) {
        return new TeamFixture(id, name, logo, goals);
    }

    private Venue toVenue(FixtureEntry entry) {
        if (entry.fixture().venue() == null) return null;
        return new Venue(
                entry.fixture().venue().name(),
                entry.fixture().venue().city()
        );
    }
}
