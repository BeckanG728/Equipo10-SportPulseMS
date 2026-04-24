package es.bytescolab.ms_standings.controller;

import es.bytescolab.ms_standings.service.StandingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/standings")
@RequiredArgsConstructor
public class StandingController {
    private final StandingService standingService;
}
