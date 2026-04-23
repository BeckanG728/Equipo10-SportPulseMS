package es.bytescolab.ms_leagues.exception;

public class LeagueNotFoundException extends RuntimeException {
    public LeagueNotFoundException(String message) {
        super(message);
    }
}
