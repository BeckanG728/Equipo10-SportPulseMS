package es.bytescolab.ms_fixtures.client.apifootball;

import es.bytescolab.ms_fixtures.client.apifootball.dto.ApiResponse;
import es.bytescolab.ms_fixtures.client.apifootball.dto.FixtureEntry;
import es.bytescolab.ms_fixtures.client.apifootball.dto.FixtureEventEntry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "football-api", url = "${api-football.host-url}")
public interface ApiFootballClient {

    /**
     * Consulta fixtures hacia api-football.
     * <p>
     * Parámetros relevantes según la documentación oficial:
     * <p>
     * - {@code league}  ID de la liga (requiere {@code season} cuando se usa). <br>
     * - {@code team}    ID del equipo (requiere {@code season} cuando se usa). <br>
     * - {@code season}  Temporada en formato YYYY. Obligatorio junto a {@code league} o {@code team}.
     * Se deriva del año de la fecha resuelta. <br>
     * - {@code date}    Fecha YYYY-MM-DD. En plan gratuito solo acepta ayer, hoy y mañana. <br>
     * - {@code status}  Código corto del estado: NS, LIVE o FT.
     */
    @GetMapping("/fixtures")
    ApiResponse<FixtureEntry> getFixtures(
            @RequestHeader("x-apisports-key") String apiKey,
            @RequestParam(value = "league", required = false) Integer league,
            @RequestParam(value = "team", required = false) Integer team,
            @RequestParam(value = "season", required = false) Integer season,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "status", required = false) String status
    );


    /**
     * Consulta fixtures en vivo hacia api-football.
     * <p>
     * Parámetros relevantes según la documentación oficial:
     * <p>
     * - Parametro {@code live}  Recibe argumento {@code all} para listar todos los partidos en vivo (estatus 1H,HT,2H)
     * <p>
     */
    @GetMapping("/fixtures")
    ApiResponse<FixtureEntry> getLiveMatches(
            @RequestHeader("x-apisports-key") String apiKey,
            @RequestParam(value = "live") String live
    );

    /**
     * Consulta fixtures/event hacia api-football.
     * <p>
     * Parámetros relevantes según la documentación oficial:
     * <p>
     * - Parametro {@code fixture}  Recibe un argumento {@code ID} para listar
     * todos los eventos del fixture seleccionado
     * <p>
     */
    @GetMapping("/fixtures/events")
    ApiResponse<FixtureEventEntry> getEvents(
            @RequestHeader("x-apisports-key") String apiKey,
            @RequestParam(value = "fixture") Integer fixture
    );
}
