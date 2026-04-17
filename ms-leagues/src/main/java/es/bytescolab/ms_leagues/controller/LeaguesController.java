package es.bytescolab.ms_leagues.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leagues")
public class LeaguesController {

    @GetMapping("/gateway-test-leagues")
    public ResponseEntity<String> test() {
        String test = "Conexion a leagues mediante gateway";
        return ResponseEntity.ok(test);
    }
}
