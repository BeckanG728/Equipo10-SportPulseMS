package es.bytescolab.ms_leagues.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CountryInfo(
        String name,
        String code,
        String flag
) {
}