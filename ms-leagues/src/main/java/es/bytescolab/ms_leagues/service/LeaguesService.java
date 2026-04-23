package es.bytescolab.ms_leagues.service;

import es.bytescolab.ms_leagues.dto.response.LeaguesResponse;

import java.util.List;

public interface LeaguesService {
    List<LeaguesResponse> getLeagues(String country, Integer season);
}
