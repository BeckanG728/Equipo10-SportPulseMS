package es.bytescolab.ms_fixtures.dto.response;

import lombok.Builder;

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
@Builder
public record FixtureEventResponse(
        Integer elapsed,
        String type,
        String detail,
        EntityRef team,
        EntityRef player,
        EntityRef assist
) {

    public record EntityRef(
            Integer id,
            String name
    ) {
    }
}
