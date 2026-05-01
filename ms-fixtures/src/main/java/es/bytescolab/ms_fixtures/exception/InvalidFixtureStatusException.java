package es.bytescolab.ms_fixtures.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFixtureStatusException extends RuntimeException {

    public InvalidFixtureStatusException(String value) {
        super("Estado de partido inválido: '" + value + "'. Los valores permitidos son: NS, LIVE, FT");
    }
}
