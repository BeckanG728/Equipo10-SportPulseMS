package es.bytescolab.ms_fixtures.mapper;

import es.bytescolab.ms_fixtures.client.apifootball.dto.FixtureEventEntry;
import es.bytescolab.ms_fixtures.dto.response.FixtureEventResponse;
import org.springframework.stereotype.Component;


/**
 * Formato de salida:
 * {
 * "elapsed": 23,
 * "type":    "Goal",
 * "detail":  "Normal Goal",
 * "team":    { "id": 529, "name": "FC Barcelona" },
 * "player":  { "id": 1100, "name": "Robert Lewandowski" },
 * "assist":  { "id": 284, "name": "Pedri" }   <- null si no hay asistencia
 * }
 * El campo "assist" se omite del JSON solo cuando es null, usando @JsonInclude.
 */
@Component
public class FixtureEventMapper {

    public FixtureEventResponse toFixtureEventResponse(FixtureEventEntry entry) {
        return FixtureEventResponse.builder()
                .elapsed(entry.time().elapsed())
                .type(entry.type())
                .detail(entry.detail())
                .team(toTeam(entry.team()))
                .player(toEntityRef(entry.player()))
                .assist(toEntityRef(entry.assist()))
                .build();
    }

    private FixtureEventResponse.EntityRef toEntityRef(FixtureEventEntry.EventPlayer p) {
        if (p == null || (p.id() == null && p.name() == null)) return null;
        return new FixtureEventResponse.EntityRef(
                p.id(),
                p.name()
        );
    }

    private FixtureEventResponse.EntityRef toTeam(FixtureEventEntry.EventTeam team) {
        return new FixtureEventResponse.EntityRef(
                team.id(),
                team.name()
        );
    }

}
