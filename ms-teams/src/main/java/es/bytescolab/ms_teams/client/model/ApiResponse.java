package es.bytescolab.ms_teams.client.model;

import java.util.List;

public record ApiResponse(
        List<TeamEntry> response
) {
}
