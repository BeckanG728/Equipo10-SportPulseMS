package es.bytescolab.ms_teams.client.model;

public record VenueInfo(
        Integer id,
        String name,
        String address,
        String city,
        Integer capacity,
        String surface,
        String image
) {
}
