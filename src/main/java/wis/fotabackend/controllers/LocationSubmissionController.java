// src/main/java/wis/fotabackend/controllers/LocationSubmissionController.java
package wis.fotabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import wis.fotabackend.dto.LocationProposal;

@RestController
@RequestMapping("/api/locations")
public class LocationSubmissionController {

    @PostMapping(value = "/proposals", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> submit(@RequestBody LocationProposal proposal) {
        System.out.println("Location proposal received: " + proposal);
        return ResponseEntity.accepted().build(); // 202 Accepted
    }
}
