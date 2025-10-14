package wis.fotabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wis.fotabackend.domains.Activation;
import wis.fotabackend.domains.Site;
import wis.fotabackend.domains.Users;
import wis.fotabackend.dto.CreateActivationDTO;
import wis.fotabackend.repositories.SiteRepository;
import wis.fotabackend.repositories.UsersRepository;
import wis.fotabackend.services.ActivationService;
import wis.fotabackend.services.ActivationServiceImpl;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/activations")
public class ActivationController {
    private final ActivationService activationService;
    private final UsersRepository usersRepository;
    private final SiteRepository siteRepository;

    public ActivationController(ActivationServiceImpl activationService,
                                UsersRepository usersRepository,
                                SiteRepository siteRepository) {
        this.activationService = activationService;
        this.usersRepository = usersRepository;
        this.siteRepository = siteRepository;
    }

    @PostMapping
    public ResponseEntity<Activation> create(@RequestBody CreateActivationDTO dto) {
        Users user = usersRepository.findByCallsign(dto.getCallsign())
                .orElseThrow(() -> new IllegalArgumentException("Unknown callsign: " + dto.getCallsign()));
        Site site = siteRepository.findById(dto.getSiteId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown site id: " + dto.getSiteId()));

        Activation a = new Activation();
        a.setUser(user);
        a.setSite(site);
        a.setStartTime(Instant.now().toString());

        a.setLogStatus(dto.getLogStatus());
        a.setSubmittedAt(Instant.now().toString());

        Activation saved = activationService.add(a);
        return ResponseEntity.created(URI.create("/api/activations/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Activation>> list(@RequestParam(required = false) String callsign,
                                                 @RequestParam(required = false) Integer siteId) {
        if (callsign != null) {
            return ResponseEntity.ok(activationService.getByUserCallsign(callsign));
        }
        if (siteId != null) {
            return ResponseEntity.ok(activationService.getBySiteCode(siteId));
        }
        return ResponseEntity.ok(activationService.getAll());
    }

    @GetMapping("/current")
    public ResponseEntity<Activation> current(@RequestParam String callsign) {
        return activationService.getCurrentByUserCallsign(callsign)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Activation> end(@PathVariable int id) {
        try {
            Activation updated = activationService.endActivation(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException notFound) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/current")
    public ResponseEntity<Activation> endCurrent(@RequestParam String callsign) {
        return activationService.endCurrentByCallsign(callsign)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
