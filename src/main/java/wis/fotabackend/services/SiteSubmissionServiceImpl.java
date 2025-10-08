package wis.fotabackend.services;

import org.springframework.stereotype.Service;
import wis.fotabackend.domains.SiteSubmission;
import wis.fotabackend.repositories.SiteSubmissionRepository;

@Service
public class SiteSubmissionServiceImpl implements SiteSubmissionService {
    private final SiteSubmissionRepository repository;

    public SiteSubmissionServiceImpl(SiteSubmissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public SiteSubmission save(SiteSubmission siteSubmission) {
        return repository.save(siteSubmission);
    }
}
