package es.bytescolab.ms_teams.service;

import es.bytescolab.ms_teams.dto.response.TeamDetailsResponse;
import es.bytescolab.ms_teams.dto.response.TeamSummaryResponse;

import java.util.List;

public interface TeamService {
    List<TeamSummaryResponse> getTeamSummary(Integer leagueId, Integer season);

    TeamDetailsResponse getTeamDetails(Integer teamId);
}
