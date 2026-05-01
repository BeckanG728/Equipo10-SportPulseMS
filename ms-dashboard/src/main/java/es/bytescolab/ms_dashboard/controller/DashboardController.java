package es.bytescolab.ms_dashboard.controller;

import es.bytescolab.ms_dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<String> health() {
        // TODO: implementar endpoints del dashboard
        return ResponseEntity.ok("ms-dashboard operativo");
    }
}
