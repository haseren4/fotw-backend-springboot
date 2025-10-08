// src/main/java/wis/fotabackend/controllers/LocationSubmissionController.java
package wis.fotabackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import wis.fotabackend.domains.SiteSubmission;
import wis.fotabackend.domains.Users;
import wis.fotabackend.dto.LocationProposal;
import wis.fotabackend.services.SiteSubmissionService;
import wis.fotabackend.services.SiteSubmissionServiceImpl;
import wis.fotabackend.services.UsersService;
import wis.fotabackend.services.UsersServiceImpl;

import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationSubmissionController {
    public UsersService userService;
    public SiteSubmissionService locSubService;
    public LocationSubmissionController(UsersServiceImpl userService, SiteSubmissionServiceImpl locSubService){
        this.locSubService = locSubService;
        this.userService = userService;
    }
    @PostMapping(value = "/proposals", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> submit(@RequestBody LocationProposal proposal) {
        System.out.println("Location proposal received: " + proposal);


            SiteSubmission siteSubmission = new SiteSubmission();
            siteSubmission.setSiteName(proposal.getSiteName());
            siteSubmission.setCategory(proposal.getCategory());
            siteSubmission.setLocation(proposal.getLocation());
            siteSubmission.setLatitude(proposal.getLatitude());
            siteSubmission.setLongitude(proposal.getLongitude());
            siteSubmission.setProposedBy(proposal.getProposedBy());
            locSubService.save(siteSubmission);
            System.out.println("Site Submission saved: " + siteSubmission);
            return ResponseEntity.accepted().build(); // 202 Accepted

    }



}
