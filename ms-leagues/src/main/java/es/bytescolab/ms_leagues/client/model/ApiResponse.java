package es.bytescolab.ms_leagues.client.model;

import java.util.List;

public record ApiResponse(
        List<LeaguesEntry> response
) {
}
