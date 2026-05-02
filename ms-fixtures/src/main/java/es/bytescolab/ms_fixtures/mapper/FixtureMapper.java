package es.bytescolab.ms_fixtures.mapper;

import es.bytescolab.ms_fixtures.client.apifootball.dto.FixtureEntry;
import es.bytescolab.ms_fixtures.dto.common.*;
import es.bytescolab.ms_fixtures.dto.response.FixtureSummaryResponse;
import es.bytescolab.ms_fixtures.dto.response.LiveMatchesResponse;
import org.springframework.stereotype.Component;

@Component
public class FixtureMapper {

    public FixtureSummaryResponse toSummaryResponse(FixtureEntry entry,
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

    public LiveMatchesResponse toLiveMatchesResponse(FixtureEntry entry) {
        Integer homeGoals = entry.goals() != null ? entry.goals().home() : null;
        Integer awayGoals = entry.goals() != null ? entry.goals().away() : null;

        return new LiveMatchesResponse(
                entry.fixture().id().intValue(),
                entry.fixture().status().elapsed(),
                toStatus(entry),
                toLeagueMatches(entry),
                toTeamLiveMatches(entry.teams().home().id(), entry.teams().home().name(), homeGoals),
                toTeamLiveMatches(entry.teams().away().id(), entry.teams().away().name(), awayGoals)
        );
    }

    private Status toStatus(FixtureEntry entry) {
        return new Status(
                entry.fixture().status().shortStatus(),
                entry.fixture().status().longStatus()
        );
    }

    private LeagueSummary toLeague(FixtureEntry entry) {
        return new LeagueSummary(
                entry.league().id(),
                entry.league().name(),
                entry.league().round()
        );
    }

    private LeagueMatches toLeagueMatches(FixtureEntry entry) {
        return new LeagueMatches(
                entry.league().id(),
                entry.league().name()
        );
    }

    private TeamSummary toTeam(Integer id, String name, String logo, Integer goals) {
        return new TeamSummary(id, name, logo, goals);
    }

    private TeamLiveMatches toTeamLiveMatches(Integer id, String name, Integer goals) {
        return new TeamLiveMatches(id, name, goals);
    }

    private Venue toVenue(FixtureEntry entry) {
        if (entry.fixture().venue() == null) return null;
        return new Venue(
                entry.fixture().venue().name(),
                entry.fixture().venue().city()
        );
    }
}
