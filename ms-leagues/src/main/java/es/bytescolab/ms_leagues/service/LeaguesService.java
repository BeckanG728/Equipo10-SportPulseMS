package es.bytescolab.ms_leagues.service;

import es.bytescolab.ms_leagues.dto.response.LeagueDetailsResponse;
import es.bytescolab.ms_leagues.dto.response.LeagueSummaryResponse;

import java.util.List;

public interface LeaguesService {
    List<LeagueSummaryResponse> getLeagues(String country, Integer season);

    LeagueDetailsResponse getLeagueDetail(Integer leagueId);
}
