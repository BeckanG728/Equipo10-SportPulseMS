package es.bytescolab.ms_teams.mapper;

import es.bytescolab.ms_teams.client.model.TeamEntry;
import es.bytescolab.ms_teams.client.model.VenueInfo;
import es.bytescolab.ms_teams.dto.response.StadiumDetails;
import es.bytescolab.ms_teams.dto.response.StadiumSummary;
import es.bytescolab.ms_teams.dto.response.TeamDetailsResponse;
import es.bytescolab.ms_teams.dto.response.TeamSummaryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TeamsMapper {
    public TeamSummaryResponse toSummaryResponse(TeamEntry entry) {
        if (entry.team() == null) return null;

        return TeamSummaryResponse.builder()
                .id(entry.team().id())
                .name(entry.team().name())
                .country(entry.team().country())
                .logo(entry.team().logo())
                .founded(entry.team().founded())
                .stadium(stadiumSummary(entry.venue()))
                .build();
    }


    private StadiumSummary stadiumSummary(VenueInfo venue) {
        return venue == null ? null :
                StadiumSummary.builder()
                        .name(venue.name())
                        .city(venue.city())
                        .capacity(venue.capacity())
                        .build();
    }

    public TeamDetailsResponse toDetailsResponse(TeamEntry entry) {
        if (entry.team() == null) return null;

        return TeamDetailsResponse.builder()
                .id(entry.team().id())
                .name(entry.team().name())
                .country(entry.team().country())
                .logo(entry.team().logo())
                .founded(entry.team().founded())
                .national(entry.team().national())
                .stadium(stadiumDetails(entry.venue()))
                .build();
    }


    private StadiumDetails stadiumDetails(VenueInfo venue) {
        return venue == null ? null :
                StadiumDetails.builder()
                        .name(venue.name())
                        .address(venue.address())
                        .city(venue.city())
                        .capacity(venue.capacity())
                        .surface(venue.surface())
                        .build();
    }

}
