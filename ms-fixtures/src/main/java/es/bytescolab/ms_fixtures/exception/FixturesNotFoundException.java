package es.bytescolab.ms_fixtures.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FixturesNotFoundException extends RuntimeException {

    public FixturesNotFoundException() {
        super("No se encontraron partidos para los filtros proporcionados");
    }
}
