package es.bytescolab.ms_standings.client.apifootballclient.dto;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "football-api", url = "${api-football.host-url}")
public interface ApiFootballClient {
}
