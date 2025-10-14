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
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown user id: " + dto.getUserId()));
        Site site = siteRepository.findById(dto.getSiteId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown site id: " + dto.getSiteId()));

        Activation a = new Activation();
        a.setUser(user);
        a.setSite(site);
        a.setStartTime(dto.getStartTime());
        a.setEndTime(dto.getEndTime());
        a.setLogStatus(dto.getLogStatus());
        a.setSubmittedAt(dto.getSubmittedAt());

        Activation saved = activationService.add(a);
        return ResponseEntity.created(URI.create("/api/activations/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Activation>> list(@RequestParam(required = false) Integer userId,
                                                 @RequestParam(required = false) Integer siteId) {
        if (userId != null) {
            return ResponseEntity.ok(activationService.getByUserId(userId));
        }
        if (siteId != null) {
            return ResponseEntity.ok(activationService.getBySiteCode(siteId));
        }
        return ResponseEntity.ok(activationService.getAll());
    }
}
