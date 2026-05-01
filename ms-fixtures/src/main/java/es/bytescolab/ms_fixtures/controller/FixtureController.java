package es.bytescolab.ms_fixtures.controller;

import es.bytescolab.ms_fixtures.dto.request.FixtureFilterRequest;
import es.bytescolab.ms_fixtures.dto.response.FixtureSummaryResponse;
import es.bytescolab.ms_fixtures.enums.FixtureStatus;
import es.bytescolab.ms_fixtures.service.FixtureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fixtures")
@RequiredArgsConstructor
public class FixtureController {

    private final FixtureService fixtureService;

    /**
     * Lista partidos filtrados por liga, equipo, fecha y/o estado.
     *
     * <ul>
     *   <li>Si no se proporciona {@code date}, se usa el día actual.</li>
     *   <li>El {@code season} se deriva del año de la fecha resuelta y se
     *       envía automáticamente cuando se filtra por {@code league} o
     *       {@code team} (requerimiento de la API externa).</li>
     *   <li>Requiere token JWT válido en la cabecera {@code Authorization}.</li>
     * </ul>
     *
     * @param league ID de la liga (opcional)
     * @param team   ID del equipo (opcional)
     * @param date   Fecha en formato YYYY-MM-DD (opcional; rango permitido: ayer, hoy, mañana)
     * @param status Estado del partido: NS, LIVE o FT (opcional)
     */
    @GetMapping
    public ResponseEntity<List<FixtureSummaryResponse>> getFixtures(
            @RequestParam(required = false) Integer league,
            @RequestParam(required = false) Integer team,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) FixtureStatus status
    ) {
        FixtureFilterRequest filter = new FixtureFilterRequest(league, team, date, status);
        return ResponseEntity.ok(fixtureService.getFixtures(filter));
    }
}
