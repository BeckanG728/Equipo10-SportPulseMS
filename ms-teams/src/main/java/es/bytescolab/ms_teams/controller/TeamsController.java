package es.bytescolab.ms_teams.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/teams")
public class TeamsController {

    @GetMapping("/gateway-test-teams")
    public ResponseEntity<String> test() {
        String test = "Conexion a teams mediante gateway";
        return ResponseEntity.ok(test);
    }
}
