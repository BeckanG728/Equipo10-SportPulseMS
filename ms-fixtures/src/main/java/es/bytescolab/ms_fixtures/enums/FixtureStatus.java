package es.bytescolab.ms_fixtures.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import es.bytescolab.ms_fixtures.exception.InvalidFixtureStatusException;

public enum FixtureStatus {

    NS("NS"),
    LIVE("LIVE"),
    FT("FT");

    private final String code;

    FixtureStatus(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    @JsonCreator
    public static FixtureStatus from(String value) {
        for (FixtureStatus status : values()) {
            if (status.code.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new InvalidFixtureStatusException(value);
    }
}
