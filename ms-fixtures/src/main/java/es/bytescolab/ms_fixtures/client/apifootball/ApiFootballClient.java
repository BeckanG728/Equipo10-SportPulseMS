package es.bytescolab.ms_fixtures.client.apifootball;

import es.bytescolab.ms_fixtures.client.apifootball.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "football-api", url = "${api-football.host-url}")
public interface ApiFootballClient {

    /**
     * Consulta fixtures contra api-football.
     * <p>
     * Parámetros relevantes según la documentación oficial:
     * <p>
     * - {@code league}  ID de la liga (requiere {@code season} cuando se usa).
     * - {@code team}    ID del equipo (requiere {@code season} cuando se usa).
     * - {@code season}  Temporada en formato YYYY. Obligatorio junto a {@code league} o {@code team}.
     * Se deriva del año de la fecha resuelta.
     * - {@code date}    Fecha YYYY-MM-DD. En plan gratuito solo acepta ayer, hoy y mañana.
     * - {@code status}  Código corto del estado: NS, LIVE o FT.
     */
    @GetMapping("/fixtures")
    ApiResponse getFixtures(
            @RequestHeader("x-apisports-key") String apiKey,
            @RequestParam(value = "league", required = false) Integer league,
            @RequestParam(value = "team", required = false) Integer team,
            @RequestParam(value = "season", required = false) Integer season,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "status", required = false) String status
    );
}
