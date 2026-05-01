package es.bytescolab.ms_fixtures.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Status(
        @JsonProperty("short")
        String shortCode,

        @JsonProperty("long")
        String longCode
) {
}
