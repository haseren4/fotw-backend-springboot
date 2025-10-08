package wis.fotabackend.services;

import wis.fotabackend.domains.SiteSubmission;
import wis.fotabackend.dto.LocationProposal;

public interface SiteSubmissionService {
    SiteSubmission save(SiteSubmission siteSubmission);
}
