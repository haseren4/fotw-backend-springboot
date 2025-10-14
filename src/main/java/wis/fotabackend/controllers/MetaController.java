package wis.fotabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/meta")
public class MetaController {

    @GetMapping("/motd")
    public ResponseEntity<Map<String, String>> motd() {
        String motd = "73s and good DX! " + Instant.now();
        return ResponseEntity.ok(Map.of("motd", motd));
    }
}
